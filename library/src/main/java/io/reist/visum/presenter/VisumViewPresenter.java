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
 * <p>
 * VisumPresenter also manages {@link rx.Subscription}s which are used to populate the attached
 * views. If the last view is removed then all subscription performed by views will be stopped.
 * <p>
 * Created by Reist on 10/15/15.
 *
 * @param <V> a type of views to be handled
 */
public abstract class VisumViewPresenter<V extends VisumView> extends VisumBasePresenter<V> {

    @Nullable
    private VisumViewHolder<V> viewHolder;
    /**
     * Attaches the given view to this presenter. For the view,
     * {@link #onViewAttached(VisumView)} will be called and the view will be able to subscribe
     * to {@link Observable}s and {@link Single}s via {@link #subscribe(Observable, Observer)},
     * {@link #subscribe(Single, Action1)} and {@link #subscribe(Single, SingleSubscriber)}.
     * <p>
     * If null is passed as a view then a view with the given id will be detached from the
     * presenter. Method {@link #onViewDetached(VisumView)} will be called. If there are no
     * remaining views after removal then all existing subscriptions made by attached views will be
     * stopped.
     *
     * @param view a MVP view; use null to stop the view with the given id
     */
    public final void setView(@Nullable V view) {

        if (viewHolder != null) {

            // detach view
            viewHolder.subscriptions.unsubscribe();
            viewHolder.subscriptions = null;
            onViewDetached(viewHolder.view);
            viewHolder.view = null;
            viewHolder = null;

            // clear subscriptions
            subscriptions.unsubscribe();
            subscriptions = null;
            onStop();

        }

        if (view != null) {

            subscriptions = new CompositeSubscription();
            onStart();

            // attach view
            viewHolder = new VisumViewHolder<>(view);
            onViewAttached(view);

        }

    }

    public void onStop() {
    }

    public void onStart() {
    }

    @NonNull
    private VisumViewHolder<V> getViewHolderOrThrow() {
        if (viewHolder == null) {
            throw new VisumViewPresenter.ViewNotFoundException();
        }
        return viewHolder;
    }

    @SuppressWarnings("unused")
    public final <T> Subscription subscribe(Observable<T> observable, Observer<? super T> observer) {
        VisumViewHolder<V> viewHolder = getViewHolderOrThrow();
        Subscription subscription = startSubscription(observable, observer);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    @SuppressWarnings("unused")
    public final <T> Subscription subscribe(Single<T> single, Action1<T> action) {
        VisumViewHolder<V> viewHolder = getViewHolderOrThrow();
        Subscription subscription = startSubscription(single, action);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }

    @SuppressWarnings("unused")
    public final <T> Subscription subscribe(Single<T> single, SingleSubscriber<T> subscriber) {
        VisumViewHolder<V> viewHolder = getViewHolderOrThrow();
        Subscription subscription = startSubscription(single, subscriber);

        viewHolder.subscriptions.add(subscription);

        return subscription;
    }


    @SuppressWarnings({"unused"})
    protected void onViewAttached(@NonNull V view) {}

    @SuppressWarnings({"unused"})
    protected void onViewDetached(@NonNull V view) {}

    @NonNull
    public final V view() {
        return getViewHolderOrThrow().view;
    }

    protected <T> void notifyCompleted(@NonNull ViewNotifier<V, T> viewNotifier) {
        if (viewHolder != null && viewHolder.view != null) {
            viewNotifier.notifyCompleted(viewHolder.view);
        }
    }

    @Override
    protected <T> void notifyResult(@NonNull ViewNotifier<V, T> viewNotifier, T t) {
        if (viewHolder != null && viewHolder.view != null) {
            viewNotifier.notifyResult(viewHolder.view, t);
        }
    }

    @Override
    protected <T> void notifyError(@NonNull ViewNotifier<V, T> viewNotifier, Throwable e) {
        if (viewHolder != null && viewHolder.view != null) {
            viewNotifier.notifyError(viewHolder.view, e);
        }
    }

    @SuppressWarnings("unused")
    public final boolean hasViewSubscriptions() {
        try {
            VisumViewHolder<V> viewHolder = getViewHolderOrThrow();
            return viewHolder.subscriptions != null && viewHolder.subscriptions.hasSubscriptions();
        } catch (ViewNotFoundException e) {
            return false;
        }
    }

    public static class ViewNotFoundException extends RuntimeException {

        public ViewNotFoundException() {
            super("View doesn't set");
        }

    }

}
