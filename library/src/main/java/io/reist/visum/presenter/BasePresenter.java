package io.reist.visum.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reist.visum.view.VisumView;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 *
 * BasePresenter manages {@link rx.Subscription}s which are used to populate the attached
 * views. If the last view is removed then all subscription performed by views will be stopped.
 *
 * Created by Reist on 10/15/15.
 *
 * @param <V> a type of views to be handled
 */

@SuppressWarnings("unused")
public abstract class BasePresenter<V extends VisumView> {

    protected CompositeSubscription subscriptions;

    protected final List<ViewHolder<V>> viewHolders = new ArrayList<>();

    protected static class ViewHolder<V> {
        protected V view;

        protected int viewId;

        protected CompositeSubscription subscriptions;

        public ViewHolder(int id, V view) {
            this.viewId = id;
            this.view = view;
            this.subscriptions = new CompositeSubscription();
        }

        public final boolean hasSubscriptions() {
            return subscriptions != null && subscriptions.hasSubscriptions();
        }

    }

    public void onStop() {}

    public void onStart() {}

    protected final <T> Subscription subscribe(ViewHolder<V> viewHolder, Observable<T> observable, Observer<? super T> observer) {
        Subscription subscription = startSubscription(observable, observer);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    protected final <T> Subscription subscribe(ViewHolder<V> viewHolder, Single<T> single, Action1<T> action) {
        Subscription subscription = startSubscription(single, action);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    protected final <T> Subscription subscribe(ViewHolder<V> viewHolder, Single<T> single, SingleSubscriber<T> subscriber) {
        Subscription subscription = startSubscription(single, subscriber);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    protected ViewHolder<V> findViewHolderByViewId(int id) {
        for (ViewHolder<V> viewHolder : viewHolders) {
            if (viewHolder.viewId == id) {
                return viewHolder;
            }
        }
        return null;
    }

    @NonNull
    protected ViewHolder<V> findViewHolderByViewIdOrThrow(int id) {
        ViewHolder<V> viewHolder = findViewHolderByViewId(id);
        if (viewHolder == null) {
            throw new ViewNotFoundException(id);
        }
        return viewHolder;
    }

    public final <T> Subscription subscribe(Observable<T> observable, @NonNull final ViewNotifier<V, T> viewNotifier) {
        Subscription subscription = startSubscription(observable, new Observer<T>() {

            @Override
            public void onCompleted() {
                notifyCompleted(viewNotifier);
            }

            @Override
            public void onError(Throwable e) {
                notifyError(viewNotifier, e);
            }

            @Override
            public void onNext(T t) {
                notifyResult(viewNotifier, t);
            }

        });

        subscriptions.add(subscription);

        return subscription;
    }

    public final <T> Subscription subscribe(Single<T> single, @NonNull final ViewNotifier<V, T> viewNotifier) {
        Subscription subscription = startSubscription(single, new SingleSubscriber<T>() {

            @Override
            public void onSuccess(T t) {
                notifyResult(viewNotifier, t);
            }

            @Override
            public void onError(Throwable e) {
                notifyError(viewNotifier, e);
            }

        });

        subscriptions.add(subscription);

        return subscription;
    }

    private <T> void notifyCompleted(@NonNull ViewNotifier<V, T> viewNotifier) {
        for (ViewHolder<V> viewHolder : viewHolders) {
            viewNotifier.notifyCompleted(viewHolder.view);
        }
    }

    private <T> void notifyResult(@NonNull ViewNotifier<V, T> viewNotifier, T t) {
        for (ViewHolder<V> viewHolder : viewHolders) {
            viewNotifier.notifyResult(viewHolder.view, t);
        }
    }

    private <T> void notifyError(@NonNull ViewNotifier<V, T> viewNotifier, Throwable e) {
        for (ViewHolder<V> viewHolder : viewHolders) {
            viewNotifier.notifyError(viewHolder.view, e);
        }
    }

    protected  <T> Subscription startSubscription(Observable<T> observable, Observer<? super T> observer) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    protected  <T> Subscription startSubscription(Single<T> single, Action1<T> action) {
        return single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
    }

    protected  <T> Subscription startSubscription(Single<T> single, SingleSubscriber<T> subscriber) {
        return single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public final int getViewCount() {
        return viewHolders.size();
    }

    public final boolean hasSubscriptions() {
        return subscriptions != null && subscriptions.hasSubscriptions();
    }

    //ViewHolder<V> viewHolder

    public static class ViewNotFoundException extends RuntimeException {

        private final int viewId;

        public ViewNotFoundException(int viewId) {
            super("No view with id = " + viewId);
            this.viewId = viewId;
        }

        public int getViewId() {
            return viewId;
        }

    }
}
