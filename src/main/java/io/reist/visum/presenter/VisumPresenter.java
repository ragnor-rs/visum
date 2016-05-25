/*
 * Copyright (c) 2015  Zvooq LTD.
 * Authors: Renat Sarymsakov, Dmitriy Mozgin, Denis Volyntsev.
 *
 * This file is part of Visum.
 *
 * Visum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Visum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Visum.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.reist.visum.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
 * A MVP presenter which is capable of handling multiple views.
 *
 * VisumPresenter also manages {@link rx.Subscription}s which are used to populate the attached
 * views. If the last view is removed then all subscription performed by views will be stopped.
 *
 * Created by Reist on 10/15/15.
 *
 * @param <V> a type of views to be handled
 */
public abstract class VisumPresenter<V extends VisumView> {

    /**
     * View id is for old visum implementations. Do not rely on this constant in new code.
     *
     * @deprecated  specify view ids explicitly
     */
    @Deprecated
    public static final int VIEW_ID_DEFAULT = 0;

    private static class ViewHolder<V> {

        private V view;
        private int viewId;
        private CompositeSubscription subscriptions;

        public ViewHolder(int id, V view) {
            this.viewId = id;
            this.view = view;
            this.subscriptions = new CompositeSubscription();
        }

    }

    private final List<ViewHolder<V>> viewHolders = new ArrayList<>();

    private ViewHolder<V> findViewHolderByViewId(int id) {
        for (ViewHolder<V> viewHolder : viewHolders) {
            if (viewHolder.viewId == id) {
                return viewHolder;
            }
        }
        return null;
    }

    private ViewHolder<V> findViewHolderByView(V v) {
        for (ViewHolder<V> viewHolder : viewHolders) {
            if (viewHolder.view == v) {
                return viewHolder;
            }
        }
        return null;
    }

    /**
     * Attaches the given view to this presenter. For the view,
     * {@link #onViewAttached(int, VisumView)} will be called and the view will be able to subscribe
     * to {@link Observable}s and {@link Single}s via {@link #subscribe(Observable, Observer)},
     * {@link #subscribe(Single, Action1)} and {@link #subscribe(Single, SingleSubscriber)}.
     *
     * If null is passed as a view then a view with the given id will be detached from the
     * presenter. Method {@link #onViewDetached(int, VisumView)} will be called. If there are no
     * remaining views after removal then all existing subscriptions made by attached views will be
     * stopped.
     *
     * @param id        an id used by the presenter to distinguish the view from the others
     * @param view      a MVP view; use null to detach the view with the given id
     */
    public final void setView(int id, @Nullable V view) {

        ViewHolder<V> viewHolder = findViewHolderByViewId(id);

        // remove the old view
        if (viewHolder != null) {
            viewHolder.subscriptions.unsubscribe();
            onViewDetached(id, viewHolder.view);
            viewHolders.remove(viewHolder);
        }

        if (view == null) {
            return;
        }

        // attach the given view
        viewHolders.add(new ViewHolder<>(id, view));
        onViewAttached(id, view);

    }

    @NonNull
    private ViewHolder<V> findViewHolderByViewIdOrThrow(int id) {
        ViewHolder<V> viewHolder = findViewHolderByViewId(id);
        if (viewHolder == null) {
            throw new IllegalStateException("No view with id = " + id);
        }
        return viewHolder;
    }

    @NonNull
    private ViewHolder<V> findViewHolderByViewOrThrow(V view) {
        ViewHolder<V> viewHolder = findViewHolderByView(view);
        if (viewHolder == null) {
            throw new IllegalStateException(view + " is not registered under " + this);
        }
        return viewHolder;
    }

    public final <T> void subscribe(int viewId, Observable<T> observable, Observer<? super T> observer) {
        ViewHolder<V> viewHolder = findViewHolderByViewIdOrThrow(viewId);
        subscribe(observable, observer, viewHolder);
    }

    public final <T> void subscribe(int viewId, Single<T> single, Action1<T> action) {
        ViewHolder<V> viewHolder = findViewHolderByViewIdOrThrow(viewId);
        subscribe(single, action, viewHolder);
    }

    public final <T> void subscribe(int viewId, Single<T> single, SingleSubscriber<T> subscriber) {
        ViewHolder<V> viewHolder = findViewHolderByViewIdOrThrow(viewId);
        subscribe(single, subscriber, viewHolder);
    }

    public final <T> void subscribe(V view, Observable<T> observable, Observer<? super T> observer) {
        ViewHolder<V> viewHolder = findViewHolderByViewOrThrow(view);
        subscribe(observable, observer, viewHolder);
    }

    public final <T> void subscribe(V view, Single<T> single, Action1<T> action) {
        ViewHolder<V> viewHolder = findViewHolderByViewOrThrow(view);
        subscribe(single, action, viewHolder);
    }

    public final <T> void subscribe(V view, Single<T> single, SingleSubscriber<T> subscriber) {
        ViewHolder<V> viewHolder = findViewHolderByViewOrThrow(view);
        subscribe(single, subscriber, viewHolder);
    }

    public final <T> void subscribeAll(Observable<T> observable, @NonNull final ViewNotifier<V, T> viewNotifier) {
        startSubscription(observable, new Observer<T>() {

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
    }

    public final <T> void subscribeAll(Single<T> single, @NonNull final ViewNotifier<V, T> viewNotifier) {
         startSubscription(single, new SingleSubscriber<T>() {

            @Override
            public void onSuccess(T t) {
                notifyResult(viewNotifier, t);
            }

            @Override
            public void onError(Throwable e) {
                notifyError(viewNotifier, e);
            }

        });
    }

    private <T> void subscribe(Observable<T> observable, Observer<? super T> observer, ViewHolder<V> viewHolder) {
        viewHolder.subscriptions.add(startSubscription(observable, observer));
    }

    private <T> void subscribe(Single<T> single, Action1<T> action, ViewHolder<V> viewHolder) {
        viewHolder.subscriptions.add(startSubscription(single, action));
    }

    private <T> void subscribe(Single<T> single, SingleSubscriber<T> subscriber, ViewHolder<V> viewHolder) {
        viewHolder.subscriptions.add(startSubscription(single, subscriber));
    }

    private <T> Subscription startSubscription(Observable<T> observable, Observer<? super T> observer) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    private <T> Subscription startSubscription(Single<T> single, Action1<T> action) {
        return single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
    }

    private <T> Subscription startSubscription(Single<T> single, SingleSubscriber<T> subscriber) {
        return single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @SuppressWarnings({"unused", "deprecation"})
    protected void onViewAttached(int id, @NonNull V view) {
        onViewAttached();
    }

    @SuppressWarnings({"unused", "deprecation"})
    protected void onViewDetached(int id, @NonNull V view) {
        onViewDetached();
    }

    @NonNull
    public final V view(int id) {
        return findViewHolderByViewIdOrThrow(id).view;
    }

    /**
     * @deprecated use {@link #setView(int, VisumView)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    public final void setView(V view) {
        setView(VIEW_ID_DEFAULT, view);
    }

    /**
     * @deprecated use {@link #onViewAttached(int, VisumView)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    protected void onViewAttached() {}

    /**
     * @deprecated use {@link #onViewDetached(int, VisumView)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    protected void onViewDetached() {}

    /**
     * @deprecated use {@link #view(int)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    @NonNull
    public final V view() {
        return view(VIEW_ID_DEFAULT);
    }

    /**
     * @deprecated use {@link #subscribe(int, Observable, Observer)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    public final <T> void subscribe(Observable<T> observable, Observer<? super T> observer) {
        subscribe(VIEW_ID_DEFAULT, observable, observer);
    }

    /**
     * @deprecated use {@link #subscribe(int, Single, Action1)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    public final <T> void subscribe(Single<T> single, Action1<T> action) {
        subscribe(VIEW_ID_DEFAULT, single, action);
    }
    /**
     * @deprecated use {@link #subscribe(int, Single, SingleSubscriber)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    public final <T> void subscribe(Single<T> single, SingleSubscriber<T> subscriber) {
        subscribe(VIEW_ID_DEFAULT, single, subscriber);
    }

    public final void forEach(@NonNull Action1<V> action1) {
        for (ViewHolder<V> viewHolder : viewHolders) {
            action1.call(viewHolder.view);
        }
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

}
