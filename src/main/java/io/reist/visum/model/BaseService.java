package io.reist.visum.model;

import java.util.List;

import io.reist.visum.Visum;
import rx.Observable;

/**
 * Created by Reist on 12/2/15.
 */
public interface BaseService<T> {

    Observable<? extends Response<List<T>>> list();

    Observable<? extends Response<T>> byId(Long id); //cur this is not true. Api will not wrap simple model into a wrapper

    Observable<? extends Response<List<T>>> save(List<T> list);

    Observable<? extends Response<T>> save(T t);

    Observable<? extends Response<Integer>> delete(Long id);

    Response<List<T>> saveSync(List<T> list);

    Response<T> saveSync(T t);

    Visum visum();

}
