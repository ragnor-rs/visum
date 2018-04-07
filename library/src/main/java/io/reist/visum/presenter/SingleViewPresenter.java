package io.reist.visum.presenter;

import android.support.annotation.NonNull;

import io.reist.visum.view.VisumView;
import rx.Completable;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * A MVP presenter for handling only one view.
 *
 * Created by 4xes on 23/11/16.
 *
 * @param <V> a type of view to be handled
 */
@SuppressWarnings({"unused"})
public abstract class SingleViewPresenter<V extends VisumView> extends VisumPresenter<V> {

    public static final int DEFAULT_VIEW_ID = 0;

    public final void setView(V view) {
        setView(DEFAULT_VIEW_ID, view);
    }

    @Override
    protected final void onViewAttached(int id, @NonNull V view) {
        onViewAttached(view);
    }

    @Override
    protected final void onViewDetached(int id, @NonNull V view) {
        onViewDetached(view);
    }

    protected void onViewAttached(@NonNull V view) {}

    protected void onViewDetached(@NonNull V view) {}

    @Override
    public final void onStop() {}

    @Override
    public final void onStart() {}

    public final void withView(Action1<V> action1) {
        withView(DEFAULT_VIEW_ID, action1);
    }


    //region Observable

    public final <T> Subscription subscribe(Observable<T> observable, Observer<? super T> observer) {
        return subscribe(DEFAULT_VIEW_ID, observable, observer);
    }

    public final <T> Subscription subscribe(Observable<T> observable, Action1<T> action, Action1<Throwable> error) {
        return subscribe(DEFAULT_VIEW_ID, observable, action, error);
    }

    //endregion


    //region Single

    public final <T> Subscription subscribe(Single<T> single, SingleSubscriber<T> subscriber) {
        return subscribe(DEFAULT_VIEW_ID, single, subscriber);
    }

    public final <T> Subscription subscribe(Single<T> single, Action1<T> action, Action1<Throwable> onError) {
        return subscribe(DEFAULT_VIEW_ID, single, action, onError);
    }

    //endregion


    //region Completable

    public final Subscription subscribe(Completable completable, Action0 onComplete, Action1<Throwable> onError) {
        return subscribe(DEFAULT_VIEW_ID, completable, onComplete, onError);
    }

    //endregion


}