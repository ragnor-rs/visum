package io.reist.visum.presenter;

import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SubscriptionsHelper {

    public static <T> Subscription startSubscription(Observable<T> observable, Observer<? super T> observer) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    public static <T> Subscription startSubscription(Single<T> single, Action1<T> action) {
        return single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
    }

    public static <T> Subscription startSubscription(Single<T> single, SingleSubscriber<T> subscriber) {
        return single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static <V, T> Subscription subscribe(BasePresenter.ViewHolder<V> viewHolder, Observable<T> observable, Observer<? super T> observer) {
        Subscription subscription = SubscriptionsHelper.startSubscription(observable, observer);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    public static <V, T> Subscription subscribe(BasePresenter.ViewHolder<V> viewHolder, Single<T> single, Action1<T> action) {
        Subscription subscription = SubscriptionsHelper.startSubscription(single, action);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    public static <V, T> Subscription subscribe(BasePresenter.ViewHolder<V> viewHolder, Single<T> single, SingleSubscriber<T> subscriber) {
        Subscription subscription = SubscriptionsHelper.startSubscription(single, subscriber);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }
}
