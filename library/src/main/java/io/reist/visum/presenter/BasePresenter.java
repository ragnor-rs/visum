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

   private CompositeSubscription subscriptions;

   private final List<ViewHolder<V>> viewHolders = new ArrayList<>();

   protected static class ViewHolder<V> {
        protected V view;

        protected int viewId;

        protected CompositeSubscription subscriptions;

        public ViewHolder(int id, V view) {
            this.viewId = id;
            this.view = view;
            this.subscriptions = new CompositeSubscription();
        }

        public boolean hasSubscriptions() {
            return subscriptions != null && subscriptions.hasSubscriptions();
        }
    }

    final V removeView(int id){

        ViewHolder<V> viewHolder = findViewHolderByViewId(id);
        if (viewHolder != null) {

            V removedView = viewHolder.view;
            viewHolder.subscriptions.unsubscribe();
            viewHolder.subscriptions = null;
            viewHolders.remove(viewHolder);

            return removedView;
        } else {
            return null;
        }
    }

    final boolean addView(int id, @NonNull V view){

        if (!hasViews()) {
            subscriptions = new CompositeSubscription();
            onStart();
        }

        viewHolders.add(new ViewHolder<>(id, view));

        return true;
    }

    public void onStop() {}

    public void onStart() {}

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
        Subscription subscription = SubscriptionsHelper.subscribe(observable, new Observer<T>() {

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
        Subscription subscription = SubscriptionsHelper.subscribe(single, new SingleSubscriber<T>() {

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


    final int getViewCount() {
        return viewHolders.size();
    }

    final boolean hasViews(){
        return getViewCount() > 0;
    }

    public final boolean hasSubscriptions() {
        return subscriptions != null && subscriptions.hasSubscriptions();
    }

    void clearSubscriptions(){
        subscriptions.unsubscribe();
        subscriptions = null;
    }

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
