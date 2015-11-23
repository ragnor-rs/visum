package io.reist.visum.model;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.List;

import io.reist.visum.Error;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Reist on 11/2/15.
 * Combines local and remote services
 */
public abstract class CachedService<T> extends AbstractBaseService<T> {

    protected final BaseService<T> local;
    protected final BaseService<T> remote;

    public CachedService(BaseService<T> local, BaseService<T> remote) {
        this.local = local;
        this.remote = remote;
    }

    /**
     * @return - data from local service and initiates remote service.
     * Remote service can end up with error (f.e. network error), which will be emitted wrapped in Response.Error
     * On success remote service saves data to local service, which should emit updated data immediately
     */
    @RxLogObservable
    @Override
    public final Observable<Response<List<T>>> list() {
        return Observable.merge(
                local.list(),
                remote.list().compose(new SaveAndEmitErrorsListTransformer<>(local)))
                .filter(new FilterListResponse<>());
    }

    /**
     * @see CachedService#list()
     */
    @RxLogObservable
    @Override
    public final Observable<Response<T>> byId(Long id) {
        return Observable.merge(
                local.byId(id),
                remote.byId(id).compose(new SaveAndEmitErrorsTransformer<>(local)))
                .filter(new FilterResponse<>());
    }

    /**
     * Puts data to local and then to remote services sequentially
     *
     * @param list - to save
     * @return num of updated items
     */
    @RxLogObservable
    @Override
    public final Observable<Response<List<T>>> save(List<T> list) { //cur we are getting num of updated items, but what about rest response?
        return Observable.concat(local.save(list), remote.save(list));
    }

    /**
     * Puts data to local and then to remote services sequentially
     *
     * @param t - object to save
     * @return boolean - whether data saved successfully
     */
    @RxLogObservable
    @Override
    public final Observable<Response<T>> save(T t) {
        return Observable.concat(
                local.save(t).first(),
                remote.save(t));
    }

    @Override
    public Observable<Integer> delete(Long id) {
        return Observable.concat(local.delete(id), remote.delete(id));
    }

    @Override
    public Response<List<T>> saveSync(List<T> list) {
        return local.saveSync(list);
    }

    @Override
    public Response<T> saveSync(T t) {
        return local.saveSync(t);
    }

    // ----

    public static class FilterListResponse<T> implements Func1<Response<List<T>>, Boolean> {

        @Override
        public Boolean call(Response<List<T>> response) {
            return response.getData() != null && !response.getData().isEmpty() || !response.isSuccessful();
        }

    }

    public static class FilterResponse<T> implements Func1<Response<T>, Boolean> {

        @Override
        public Boolean call(Response<T> response) {
            return response.getData() != null || !response.isSuccessful();
        }

    }

    private static class SaveTransformer<T> {

        protected final BaseService<T> service;

        public SaveTransformer(BaseService<T> service) {
            this.service = service;
        }

    }

    public static class SaveAndEmitErrorsTransformer<T>
            extends SaveTransformer<T>
            implements Observable.Transformer<Response<T>, Response<T>> {

        public SaveAndEmitErrorsTransformer(BaseService<T> service) {
            super(service);
        }

        @Override
        public Observable<Response<T>> call(Observable<Response<T>> observable) {
            return observable
                    .doOnNext(r -> service.saveSync(r.getData()))
                    .filter(r -> !r.isSuccessful())
                    .onErrorResumeNext((t) -> {
                        Response<T> responseWithError = new Response<>();
                        responseWithError.setError(new io.reist.visum.Error(t));
                        return Observable.just(responseWithError);
                    });
        }
    }


    public static class SaveAndEmitErrorsListTransformer<T>
            extends SaveTransformer<T>
            implements Observable.Transformer<Response<List<T>>, Response<List<T>>>

    {

        public SaveAndEmitErrorsListTransformer(BaseService<T> service) {
            super(service);
        }

        @Override
        public Observable<Response<List<T>>> call(Observable<Response<List<T>>> observable) {
            return observable
                    .doOnNext(r -> service.saveSync(r.getData()))
                    .filter(r -> !r.isSuccessful())
                    .onErrorResumeNext((t) -> {
                        Response<List<T>> responseWithError = new Response<>();
                        responseWithError.setError(new Error(t));
                        return Observable.just(responseWithError);
                    });
        }

    }

}
