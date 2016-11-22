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
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * A MVP presenter which is capable of handling multiple views.
 *
 * VisumPresenter also manages {@link Subscription}s which are used to populate the attached
 * views. If the last view is removed then all subscription performed by views will be stopped.
 *
 * Created by Reist on 10/15/15.
 *
 * @param <V> a type of views to be handled
 */
public abstract class VisumViewsPresenter<V extends VisumView> extends VisumBasePresenter<V> {

    private final List<VisumViewHolder<V>> viewHolders = new ArrayList<>();

    private VisumViewHolder<V> findViewHolderByViewId(int viewId) {
        for (VisumViewHolder<V> viewHolder : viewHolders) {
            if (viewHolder.view.getViewId() == viewId) {
                return viewHolder;
            }
        }
        return null;
    }

    /**
     * Attaches the given view to this presenter. For the view,
     * {@link #onViewAttached(int, VisumView)} will be called and the view will be able to subscribe
     * to {@link Observable}s and {@link Single}s via {@link #subscribe(int, Observable, Observer)},
     * {@link #subscribe(int, Single, Action1)} and {@link #subscribe(int, Single, SingleSubscriber)}.
     *
     * If null is passed as a view then a view with the given id will be detached from the
     * presenter. Method {@link #onViewDetached(int, VisumView)} will be called. If there are no
     * remaining views after removal then all existing subscriptions made by attached views will be
     * stopped.
     *
     * @param viewId an id used by the presenter to distinguish the view from the others
     * @param view   a MVP view; use null to stop the view with the given id
     */
    public final void setView(int viewId, @Nullable V view) {

        VisumViewHolder<V> viewHolder = findViewHolderByViewId(viewId);

        if (viewHolder != null) {

            // remove the old view
            viewHolder.subscriptions.unsubscribe();
            viewHolder.subscriptions = null;
            onViewDetached(viewHolder.view.getViewId(), viewHolder.view);
            viewHolders.remove(viewHolder);

            if (getViewCount() == 0) {
                subscriptions.unsubscribe();
                subscriptions = null;
                onStop();
            }

        }

        if (view != null) {

            if (getViewCount() == 0) {
                subscriptions = new CompositeSubscription();
                onStart();
            }

            // start the given view
            viewHolders.add(new VisumViewHolder<>(view));
            onViewAttached(view.getViewId(), view);

        }

    }

    public void onStop() {}

    public void onStart() {}

    @NonNull
    private VisumViewHolder<V> findViewHolderByViewIdOrThrow(int id) {
        VisumViewHolder<V> viewHolder = findViewHolderByViewId(id);
        if (viewHolder == null) {
            throw new ViewNotFoundException(id);
        }
        return viewHolder;
    }

    @SuppressWarnings("unused")
    public final <T> Subscription subscribe(int viewId, Observable<T> observable, Observer<? super T> observer) {
        VisumViewHolder<V> viewHolder = findViewHolderByViewIdOrThrow(viewId);
        Subscription subscription = startSubscription(observable, observer);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    @SuppressWarnings("unused")
    public final <T> Subscription subscribe(int viewId, Single<T> single, Action1<T> action) {
        VisumViewHolder<V> viewHolder = findViewHolderByViewIdOrThrow(viewId);
        Subscription subscription = startSubscription(single, action);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    @SuppressWarnings("unused")
    public final <T> Subscription subscribe(int viewId, Single<T> single, SingleSubscriber<T> subscriber) {
        VisumViewHolder<V> viewHolder = findViewHolderByViewIdOrThrow(viewId);
        Subscription subscription = startSubscription(single, subscriber);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    @SuppressWarnings({"unused"})
    protected void onViewAttached(int viewId, @NonNull V view) {}

    @SuppressWarnings({"unused"})
    protected void onViewDetached(int viewId, @NonNull V view) {}

    @NonNull
    public final V view(int id) {
        return findViewHolderByViewIdOrThrow(id).view;
    }

    @SuppressWarnings("unused")
    public final void forEachView(@NonNull Action1<V> action1) {
        for (VisumViewHolder<V> viewHolder : viewHolders) {
            action1.call(viewHolder.view);
        }
    }

    @Override
    protected <T> void notifyCompleted(@NonNull ViewNotifier<V, T> viewNotifier) {
        for (VisumViewHolder<V> viewHolder : viewHolders) {
            viewNotifier.notifyCompleted(viewHolder.view);
        }
    }

    @Override
    protected <T> void notifyResult(@NonNull ViewNotifier<V, T> viewNotifier, T t) {
        for (VisumViewHolder<V> viewHolder : viewHolders) {
            viewNotifier.notifyResult(viewHolder.view, t);
        }
    }

    @Override
    protected <T> void notifyError(@NonNull ViewNotifier<V, T> viewNotifier, Throwable e) {
        for (VisumViewHolder<V> viewHolder : viewHolders) {
            viewNotifier.notifyError(viewHolder.view, e);
        }
    }

    public final int getViewCount() {
        return viewHolders.size();
    }

    @SuppressWarnings("unused")
    public final boolean hasSubscriptions(int viewId) {
        try {
            VisumViewHolder<V> viewHolder = findViewHolderByViewIdOrThrow(viewId);
            return viewHolder.subscriptions != null && viewHolder.subscriptions.hasSubscriptions();
        } catch (ViewNotFoundException e) {
            return false;
        }
    }

    public static class ViewNotFoundException extends RuntimeException {

        private final int viewId;

        public ViewNotFoundException(int viewId) {
            super("No view with id = " + viewId);
            this.viewId = viewId;
        }

        @SuppressWarnings("unused")
        public int getViewId() {
            return viewId;
        }

    }

}
