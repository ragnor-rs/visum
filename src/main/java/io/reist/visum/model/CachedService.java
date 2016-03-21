/*
 * Copyright (c) 2015  Zvooq LTD.
 * Authors: Renat Sarymsakov, Dmitriy Mozgin, Denis Volyntsev.
 *
 * This file is part of Visum.
 *
 * Visum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Visum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Visum.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.reist.visum.model;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Reist on 11/2/15.
 * Combines local and remote services
 */
public abstract class CachedService<T> extends BaseService<T> {

    protected final VisumService<T> local;
    protected final VisumService<T> remote;

    public CachedService(VisumService<T> local, VisumService<T> remote) {
        this.local = local;
        this.remote = remote;
    }

    /**
     * @return - data from local service and initiates remote service.
     * Remote service can end up with error (f.e. network error), which will be emitted wrapped in Response.Error
     * On success remote service saves data to local service, which should emit updated data immediately
     */
    @Override
    public final Observable<VisumResponse<List<T>>> list() {
        return Observable
                .merge(
                        local.list(),
                        remote.list().compose(new SaveAndEmitErrorsListTransformer<>(local)))
                .filter(new ListResponseFilter<>())
                .first();
    }

    /**
     * @see CachedService#list()
     */
    @Override
    public final Observable<VisumResponse<T>> byId(Long id) {
        return Observable
                .merge(
                        local.byId(id),
                        remote.byId(id).compose(new SaveAndEmitErrorsTransformer<>(local))
                )
                .filter(new ResponseFilter<>())
                .first();
    }

    /**
     * Puts data to local and then to remote services sequentially
     *
     * @param list - to save
     * @return num of updated items
     */
    @Override
    public final Observable<VisumResponse<List<T>>> save(List<T> list) { //cur we are getting num of updated items, but what about rest response?
        return Observable.concat(local.save(list), remote.save(list));
    }

    /**
     * Puts data to local and then to remote services sequentially
     *
     * @param t - object to save
     * @return boolean - whether data saved successfully
     */
    @Override
    public final Observable<VisumResponse<T>> save(T t) {
        return Observable.concat(
                local.save(t).first(),
                remote.save(t));
    }

    @Override
    public Observable<VisumResponse<Integer>> delete(Long id) {
        return Observable.concat(local.delete(id), remote.delete(id));
    }

    @Override
    public VisumResponse<List<T>> saveSync(List<T> list) {
        return local.saveSync(list);
    }

    @Override
    public VisumResponse<T> saveSync(T t) {
        return local.saveSync(t);
    }

    // ----

    public static class ListResponseFilter<T> implements Func1<VisumResponse<List<T>>, Boolean> {

        @Override
        public Boolean call(VisumResponse<List<T>> response) {
            return response.getResult() != null && !response.getResult().isEmpty() || !response.isSuccessful();
        }

    }

    public static class ResponseFilter<T> implements Func1<VisumResponse<T>, Boolean> {

        @Override
        public Boolean call(VisumResponse<T> response) {
            return response.getResult() != null || !response.isSuccessful();
        }

    }

    private static class SaveTransformer<T> {

        protected final VisumService<T> service;

        public SaveTransformer(VisumService<T> service) {
            this.service = service;
        }

    }

    public static class SaveAndEmitErrorsTransformer<T>
            extends SaveTransformer<T>
            implements Observable.Transformer<VisumResponse<T>, VisumResponse<T>> {

        public SaveAndEmitErrorsTransformer(VisumService<T> service) {
            super(service);
        }

        @Override
        public Observable<VisumResponse<T>> call(Observable<VisumResponse<T>> observable) {
            return observable
                    .doOnNext(r -> service.saveSync(r.getResult()))
                    .filter(r -> !r.isSuccessful())
                    .onErrorResumeNext(t -> Observable.just(new BaseResponse<>(new BaseError(t))));
        }
    }


    public static class SaveAndEmitErrorsListTransformer<T>
            extends SaveTransformer<T>
            implements Observable.Transformer<VisumResponse<List<T>>, VisumResponse<List<T>>> {

        public SaveAndEmitErrorsListTransformer(VisumService<T> service) {
            super(service);
        }

        @Override
        public Observable<VisumResponse<List<T>>> call(Observable<VisumResponse<List<T>>> observable) {
            return observable
                    .doOnNext(r -> service.saveSync(r.getResult()))
                    .filter(r -> !r.isSuccessful())
                    .onErrorResumeNext(t -> Observable.just(new BaseResponse<>(new BaseError(t))));
        }

    }

}
