package io.reist.visum.model;

import java.util.List;

import rx.Observable;

/**
 * Created by Reist on 12/2/15.
 */
public interface BaseService<T> {

    Observable<Response<List<T>>> list();

    Observable<Response<T>> byId(Long id); //cur this is not true. Api will not wrap simple model into a wrapper

    Observable<Response<List<T>>> save(List<T> list);

    Observable<Response<T>> save(T t);

    Observable<Integer> delete(Long id);

    Response<List<T>> saveSync(List<T> list);

    Response<T> saveSync(T t);

}
