package io.reist.visum.presenter;

import android.support.annotation.NonNull;

import io.reist.visum.view.VisumView;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * A MVP presenter for handling only one view.
 *
 * Created by 4xes on 23/11/16.
 *
 * @param <V> a type of view to be handled
 */
@SuppressWarnings({"unused"})
public abstract class SingleViewPresenter<V extends VisumView> extends VisumPresenter<V>{

    public static final int VIEW_ID_DEFAULT = 0;

    public final void setView(V view) {
        setView(VIEW_ID_DEFAULT, view);
    }

    @Override
    protected void onViewAttached(int id, @NonNull V view) {
        onViewAttached();
    }

    @Override
    protected void onViewDetached(int id, @NonNull V view) {
        onViewDetached();
    }

    protected void onViewAttached() {}

    protected void onViewDetached() {}

    @NonNull
    public final V view() {
        return view(VIEW_ID_DEFAULT);
    }

    public final <T> Subscription subscribe(Observable<T> observable, Observer<? super T> observer) {
        return subscribe(VIEW_ID_DEFAULT, observable, observer);
    }

    public final <T> Subscription subscribe(Single<T> single, Action1<T> action) {
        return subscribe(VIEW_ID_DEFAULT, single, action);
    }

    public final <T> Subscription subscribe(Single<T> single, SingleSubscriber<T> subscriber) {
        return subscribe(VIEW_ID_DEFAULT, single, subscriber);
    }

    public final boolean hasViewSubscriptions() {
        return hasSubscriptions(VIEW_ID_DEFAULT);
    }


}