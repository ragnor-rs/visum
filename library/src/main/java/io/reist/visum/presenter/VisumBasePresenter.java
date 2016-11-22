package io.reist.visum.presenter;

import android.support.annotation.NonNull;

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

public abstract class VisumBasePresenter<V extends VisumView> {

    protected CompositeSubscription subscriptions;

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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

    <T> Subscription startSubscription(Observable<T> observable, Observer<? super T> observer) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    <T> Subscription startSubscription(Single<T> single, Action1<T> action) {
        return single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
    }

    <T> Subscription startSubscription(Single<T> single, SingleSubscriber<T> subscriber) {
        return single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    protected abstract <T> void notifyCompleted(@NonNull ViewNotifier<V, T> viewNotifier);

    protected abstract <T> void notifyResult(@NonNull ViewNotifier<V, T> viewNotifier, T t);

    protected abstract <T> void notifyError(@NonNull ViewNotifier<V, T> viewNotifier, Throwable e);

    @SuppressWarnings("unused")
    public final boolean hasSubscriptions() {
        return subscriptions != null && subscriptions.hasSubscriptions();
    }
}
