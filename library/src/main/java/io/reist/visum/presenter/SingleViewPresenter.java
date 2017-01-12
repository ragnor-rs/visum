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
public abstract class SingleViewPresenter<V extends VisumView> extends VisumPresenter<V> {

    public static final int DEFAULT_VIEW_ID = 0;

    public final void setView(V view) {
        setView(DEFAULT_VIEW_ID, view);
    }

    @Override
    protected void onViewAttached(int id, @NonNull V view) {
        onViewAttached(view);
    }

    @Override
    protected void onViewDetached(int id, @NonNull V view) {
        onViewDetached(view);
    }

    protected void onViewAttached(@NonNull V view) {}

    protected void onViewDetached(@NonNull V view) {}

    @Override
    final public void onStop() {}

    @Override
    final public void onStart() {}

    @NonNull
    public final V view() {
        return view(DEFAULT_VIEW_ID);
    }

    public final <T> Subscription subscribe(Observable<T> observable, Observer<? super T> observer) {
        return subscribe(DEFAULT_VIEW_ID, observable, observer);
    }

    public final <T> Subscription subscribe(Single<T> single, Action1<T> action) {
        return subscribe(DEFAULT_VIEW_ID, single, action);
    }

    public final <T> Subscription subscribe(Single<T> single, SingleSubscriber<T> subscriber) {
        return subscribe(DEFAULT_VIEW_ID, single, subscriber);
    }


}