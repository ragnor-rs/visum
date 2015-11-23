package io.reist.visum.presenter;

import android.content.Context;

import io.reist.visum.view.BaseView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Reist on 10/15/15.
 */
public abstract class BasePresenter<V extends BaseView> {

    private CompositeSubscription subscriptions;
    private V view;

    public final void setView(V view) {
        this.view = view;
        if (view == null) {
            subscriptions.unsubscribe();
            onViewDetached();
        } else {
            subscriptions = new CompositeSubscription();
            onViewAttached();
        }
    }

    protected final <T> void subscribe(Observable<T> observable, Observer<T> observer) {
        subscriptions.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(observer)
        );
    }

    protected abstract void onViewAttached();

    protected void onViewDetached() {}

    public final V view() {
        return view;
    }

    public final Context getContext() {
        return view == null ? null : view.context();
    }

}
