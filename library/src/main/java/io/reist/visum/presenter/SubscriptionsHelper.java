package io.reist.visum.presenter;


import io.reist.visum.presenter.BasePresenter.ViewHolder;
import rx.Completable;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *  A helper class for simplify work with Schedulers
 *  and make Subscriptions added to {@link ViewHolder#subscriptions}.
 * <p>
 * Created by 4xes on 25/11/16.
 */
class SubscriptionsHelper {

    public static <T> Subscription subscribe(Observable<T> observable, Observer<? super T> observer) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    public static <T> Subscription subscribe(Single<T> single, Action1<T> onSuccess) {
        return single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess);
    }

    public static <T> Subscription subscribe(Single<T> single, SingleSubscriber<T> subscriber) {
        return single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static Subscription subscribe(Completable completable, Action0 onComplete) {
        return completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete);
    }

    public static Subscription subscribe(Completable completable, Action0 onComplete, Action1<? super Throwable> onError) {
        return completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete, onError);
    }

    public static <V, T> Subscription subscribe(ViewHolder<V> viewHolder, Observable<T> observable, Observer<? super T> observer) {
        Subscription subscription = SubscriptionsHelper.subscribe(observable, observer);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    public static <V, T> Subscription subscribe(ViewHolder<V> viewHolder, Single<T> single, Action1<T> onSuccess) {
        Subscription subscription = SubscriptionsHelper.subscribe(single, onSuccess);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    public static <V, T> Subscription subscribe(ViewHolder<V> viewHolder, Single<T> single, SingleSubscriber<T> subscriber) {
        Subscription subscription = SubscriptionsHelper.subscribe(single, subscriber);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    public static <V> Subscription subscribe(ViewHolder<V> viewHolder, Completable completable, Action0 onComplete) {
        Subscription subscription = SubscriptionsHelper.subscribe(completable, onComplete);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    public static <V> Subscription subscribe(ViewHolder<V> viewHolder, Completable completable, Action0 onComplete, Action1<? super Throwable> onError) {
        Subscription subscription = SubscriptionsHelper.subscribe(completable, onComplete, onError);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }
}
