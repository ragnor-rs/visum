/*
 * Copyright (C) 2017 Renat Sarymsakov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.reist.sandbox.app.model;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Reist on 11/2/15.
 * Combines local and remote services
 */
public abstract class CachedService<T> implements SandboxService<T> {

    protected final SandboxService<T> local;
    protected final SandboxService<T> remote;

    public CachedService(SandboxService<T> local, SandboxService<T> remote) {
        this.local = local;
        this.remote = remote;
    }

    /**
     * @return - data from local service and initiates remote service.
     * Remote service can end up with error (f.e. network error), which will be emitted wrapped in Response.Error
     * On success remote service saves data to local service, which should emit updated data immediately
     */
    @Override
    public final Observable<SandboxResponse<List<T>>> list() {
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
    public final Observable<SandboxResponse<T>> byId(Long id) {
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
    public final Observable<SandboxResponse<List<T>>> save(List<T> list) { //cur we are getting num of updated items, but what about rest response?
        return Observable.concat(local.save(list), remote.save(list));
    }

    /**
     * Puts data to local and then to remote services sequentially
     *
     * @param t - object to save
     * @return boolean - whether data saved successfully
     */
    @Override
    public final Observable<SandboxResponse<T>> save(T t) {
        return Observable.concat(
                local.save(t).first(),
                remote.save(t));
    }

    @Override
    public Observable<SandboxResponse<Integer>> delete(Long id) {
        return Observable.concat(local.delete(id), remote.delete(id));
    }

    @Override
    public SandboxResponse<List<T>> saveSync(List<T> list) {
        return local.saveSync(list);
    }

    @Override
    public SandboxResponse<T> saveSync(T t) {
        return local.saveSync(t);
    }

    // ----

    public static class ListResponseFilter<T> implements Func1<SandboxResponse<List<T>>, Boolean> {

        @Override
        public Boolean call(SandboxResponse<List<T>> response) {
            return response.getResult() != null && !response.getResult().isEmpty() || !response.isSuccessful();
        }

    }

    public static class ResponseFilter<T> implements Func1<SandboxResponse<T>, Boolean> {

        @Override
        public Boolean call(SandboxResponse<T> response) {
            return response.getResult() != null || !response.isSuccessful();
        }

    }

    private static class SaveTransformer<T> {

        protected final SandboxService<T> service;

        public SaveTransformer(SandboxService<T> service) {
            this.service = service;
        }

    }

    public static class SaveAndEmitErrorsTransformer<T>
            extends SaveTransformer<T>
            implements Observable.Transformer<SandboxResponse<T>, SandboxResponse<T>> {

        public SaveAndEmitErrorsTransformer(SandboxService<T> service) {
            super(service);
        }

        @Override
        public Observable<SandboxResponse<T>> call(Observable<SandboxResponse<T>> observable) {
            return observable
                    .doOnNext(r -> service.saveSync(r.getResult()))
                    .filter(r -> !r.isSuccessful())
                    .onErrorResumeNext(t -> Observable.just(new SandboxResponse<>(new SandboxError(t))));
        }
    }

    public static class SaveAndEmitErrorsListTransformer<T>
            extends SaveTransformer<T>
            implements Observable.Transformer<SandboxResponse<List<T>>, SandboxResponse<List<T>>> {

        public SaveAndEmitErrorsListTransformer(SandboxService<T> service) {
            super(service);
        }

        @Override
        public Observable<SandboxResponse<List<T>>> call(Observable<SandboxResponse<List<T>>> observable) {
            return observable
                    .doOnNext(r -> service.saveSync(r.getResult()))
                    .filter(r -> !r.isSuccessful())
                    .onErrorResumeNext(t -> Observable.just(new SandboxResponse<>(new SandboxError(t))));
        }

    }

    @Override
    public void addListener(Action1<T> dataListener) {
        remote.addListener(dataListener);
    }

    @Override
    public void removeListener(Action1<T> dataListener) {
        remote.removeListener(dataListener);
    }

}
