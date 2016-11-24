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
 * A MVP presenter for handling only one view.
 *
 * Created by 4xes on 23/11/16.
 *
 * @param <V> a type of view to be handled
 */
@SuppressWarnings({"unused"})
public abstract class VisumSinglePresenter<V extends VisumView> extends BasePresenter<V>{

    public static final int VIEW_ID_DEFAULT = 0;

    /**
     * Attaches the given view to this presenter. For the view,
     * {@link #onViewAttached()} will be called and the view will be able to subscribe
     * to {@link Observable}s and {@link Single}s via {@link #subscribe(Observable, Observer)},
     * {@link #subscribe(Single, Action1)} and {@link #subscribe(Single, SingleSubscriber)}.
     *
     * If null is passed as a view then a view with the given id will be detached from the
     * presenter. Method {@link #onViewDetached()} will be called.
     * After removal view all existing subscriptions made by attached views will be
     * stopped.
     *
     * @param view a MVP view
     */
    public final void setView(@Nullable V view) {

        ViewHolder<V> viewHolder = findViewHolderByViewId(VIEW_ID_DEFAULT);

        if (viewHolder != null) {

            // remove the old view
            viewHolder.subscriptions.unsubscribe();
            viewHolder.subscriptions = null;
            onViewDetached();
            viewHolders.remove(viewHolder);

            if (hasView()) {
                subscriptions.unsubscribe();
                subscriptions = null;
                onStop();
            }

        }

        if (view != null) {

            if (hasView()) {
                subscriptions = new CompositeSubscription();
                onStart();
            }

            // start the given view
            viewHolders.add(new BasePresenter.ViewHolder<>(VIEW_ID_DEFAULT, view));
            onViewAttached();

        }
    }

    protected void onViewAttached() {}

    protected void onViewDetached() {}

    public boolean hasView(){
        return findViewHolderByViewId(VIEW_ID_DEFAULT) != null;
    }

    @NonNull
    public final V view() {
        return findViewHolderByViewIdOrThrow(VIEW_ID_DEFAULT).view;
    }

    public final <T> Subscription subscribe(Observable<T> observable, Observer<? super T> observer) {
        return subscribe(findViewHolderByViewId(VIEW_ID_DEFAULT), observable, observer);
    }

    public final <T> Subscription subscribe(Single<T> single, Action1<T> action) {
        return subscribe(findViewHolderByViewId(VIEW_ID_DEFAULT), single, action);
    }

    public final <T> Subscription subscribe(Single<T> single, SingleSubscriber<T> subscriber) {
        return subscribe(findViewHolderByViewId(VIEW_ID_DEFAULT), single, subscriber);
    }

    public final boolean hasViewSubscriptions() {
        try {
            return findViewHolderByViewIdOrThrow(VIEW_ID_DEFAULT).hasSubscriptions();
        } catch (ViewNotFoundException e) {
            return false;
        }
    }


}
