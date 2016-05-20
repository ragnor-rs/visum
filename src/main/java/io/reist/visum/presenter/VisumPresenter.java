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
public abstract class VisumPresenter<V> {

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

    /**
     * Attaches the given view to this presenter. For the view, {@link #onViewAttached(Object)} will
     * be called and the view will be able to subscribe to {@link Observable}s and {@link Single}s via
     * {@link #subscribe(Observable, Observer)}, {@link #subscribe(Single, Action1)} and
     * {@link #subscribe(Single, SingleSubscriber)}.
     *
     * If null is passed as a view then a view with the given id will be detached from the presenter.
     * Method {@link #onViewDetached(Object)} will be called. If there are no remaining views after
     * removal then all existing subscriptions made by attached views will be stopped.
     *
     * @param id        an id used by the presenter to distinguish the view from the others
     * @param view      a MVP view; use null to detach the view with the given id
     */
    public final void setView(int id, @Nullable V view) {

        ViewHolder<V> viewHolder = findViewHolderByViewId(id);

        // remove the old view
        if (viewHolder != null) {
            viewHolder.subscriptions.unsubscribe();
            onViewDetached(viewHolder.view);
            viewHolders.remove(viewHolder);
        }

        if (view == null) {
            return;
        }

        // attach the given view
        viewHolders.add(new ViewHolder<>(id, view));
        onViewAttached(view);

    }

    @NonNull
    private ViewHolder<V> findViewHolderByViewIdOrThrow(int id) {
        ViewHolder<V> viewHolder = findViewHolderByViewId(id);
        if (viewHolder == null) {
            throw new IllegalStateException("No view with id = " + id);
        }
        return viewHolder;
    }

    public final <T> void subscribe(int viewId, Observable<T> observable, Observer<? super T> observer) {
        findViewHolderByViewIdOrThrow(viewId).subscriptions.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(observer)
        );
    }

    public final <T> void subscribe(int viewId, Single<T> single, Action1<T> action) {
        findViewHolderByViewIdOrThrow(viewId).subscriptions.add(
                single
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(action)
        );
    }

    public final <T> void subscribe(int viewId, Single<T> single, SingleSubscriber<T> subscriber) {
        findViewHolderByViewIdOrThrow(viewId).subscriptions.add(
                single
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }

    @SuppressWarnings({"unused", "deprecation"})
    protected void onViewDetached(V view) {
        onViewDetached();
    }

    @SuppressWarnings({"unused", "deprecation"})
    protected void onViewAttached(V view) {
        onViewAttached();
    }

    @NonNull
    public final V findViewById(int id) {
        return findViewHolderByViewIdOrThrow(id).view;
    }

    /**
     * @deprecated use {@link #setView(int, Object)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    public final void setView(V view) {
        setView(VisumView.VIEW_ID_DEFAULT, view);
    }

    /**
     * @deprecated use {@link #onViewAttached(Object)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    protected void onViewAttached() {}

    /**
     * @deprecated use {@link #onViewDetached(Object)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    protected void onViewDetached() {}

    /**
     * @deprecated use {@link #findViewById(int)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    @NonNull
    public final V view() {
        return findViewById(VisumView.VIEW_ID_DEFAULT);
    }

    /**
     * @deprecated use {@link #subscribe(int, Observable, Observer)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    public final <T> void subscribe(Observable<T> observable, Observer<? super T> observer) {
        subscribe(VisumView.VIEW_ID_DEFAULT, observable, observer);
    }

    /**
     * @deprecated use {@link #subscribe(int, Single, Action1)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    public final <T> void subscribe(Single<T> single, Action1<T> action) {
        subscribe(VisumView.VIEW_ID_DEFAULT, single, action);
    }
    /**
     * @deprecated use {@link #subscribe(int, Single, SingleSubscriber)} instead
     */
    @Deprecated
    @SuppressWarnings({"unused", "deprecation"})
    public final <T> void subscribe(Single<T> single, SingleSubscriber<T> subscriber) {
        subscribe(VisumView.VIEW_ID_DEFAULT, single, subscriber);
    }

}
