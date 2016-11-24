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
 *
 *
 * Created by Reist on 10/15/15.
 *
 * @param <V> a type of views to be handled
 */

@SuppressWarnings({"unused"})
public abstract class VisumPresenter<V extends VisumView> extends BasePresenter<V> {


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
     * @param id        an id used by the presenter to distinguish the view from the others
     * @param view      a MVP view; use null to stop the view with the given id
     */
    public final void setView(int id, @Nullable V view) {

        ViewHolder<V> viewHolder = findViewHolderByViewId(id);

        if (viewHolder != null) {

            // remove the old view
            viewHolder.subscriptions.unsubscribe();
            viewHolder.subscriptions = null;
            onViewDetached(id, viewHolder.view);
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
            viewHolders.add(new BasePresenter.ViewHolder<>(id, view));
            onViewAttached(id, view);

        }
    }

    protected void onViewAttached(int id, @NonNull V view) {}

    protected void onViewDetached(int id, @NonNull V view) {}

    @NonNull
    public final V view(int id) {
        return findViewHolderByViewIdOrThrow(id).view;
    }

    public final <T> Subscription subscribe(int viewId, Observable<T> observable, Observer<? super T> observer) {
        return subscribe(findViewHolderByViewId(viewId), observable, observer);
    }

    public final <T> Subscription subscribe(int viewId, Single<T> single, Action1<T> action) {
        return subscribe(findViewHolderByViewId(viewId), single, action);
    }

    public final <T> Subscription subscribe(int viewId, Single<T> single, SingleSubscriber<T> subscriber) {
        return subscribe(findViewHolderByViewId(viewId), single, subscriber);
    }


    public final boolean hasViewSubscriptions(int viewId) {
        try {
            return findViewHolderByViewIdOrThrow(viewId).hasSubscriptions();
        } catch (ViewNotFoundException e) {
            return false;
        }
    }


}
