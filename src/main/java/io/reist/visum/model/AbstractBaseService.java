package io.reist.visum.model;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.List;

import io.reist.visum.Visum;
import rx.Observable;

/**
 * Created by Reist on 12/2/15.
 */
public abstract class AbstractBaseService<T> implements BaseService<T> {

    protected final Visum visum;

    public AbstractBaseService(Visum visum) {
        this.visum = visum;
    }

    @Override
    public Visum visum() {
        return visum;
    }

    @RxLogObservable
    @Override
    public Observable<? extends Response<List<T>>> save(List<T> list) {
        return Observable.just(saveSync(list));
    }

    @RxLogObservable
    @Override
    public Observable<? extends Response<T>> save(T t) {
        return Observable.just(saveSync(t));
    }

}
