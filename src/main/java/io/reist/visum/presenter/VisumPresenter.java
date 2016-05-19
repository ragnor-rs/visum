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

        public ViewHolder(int id, V view) {
            this.viewId = id;
            this.view = view;
        }

    }

    private CompositeSubscription subscriptions;

    private final List<ViewHolder<V>> viewHolders = new ArrayList<>();

    private ViewHolder<V> findViewHolderById(int id) {
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

        ViewHolder<V> viewHolder = findViewHolderById(id);

        // remove the old view
        if (viewHolder != null) {
            if (viewHolders.isEmpty()) {
                subscriptions.unsubscribe();
                subscriptions = null;
            }
            onViewDetached(viewHolder.view);
            viewHolders.remove(viewHolder);
        }

        if (view == null) {
            return;
        }

        // attach the given view
        viewHolders.add(new ViewHolder<>(id, view));
        if (subscriptions == null) {
            subscriptions = new CompositeSubscription();
        }
        onViewAttached(view);

    }

    public final <T> void subscribe(Observable<T> observable, Observer<? super T> observer) {
        subscriptions.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(observer)
        );
    }

    public final <T> void subscribe(Single<T> single, Action1<T> action) {
        subscriptions.add(
                single
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(action)
        );
    }

    public final <T> void subscribe(Single<T> single, SingleSubscriber<T> subscriber) {
        subscriptions.add(
                single
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }

    protected void onViewDetached(V view) {
        onViewDetached();
    }

    protected void onViewAttached(V view) {
        onViewAttached();
    }

    @Nullable
    public final V findViewById(int id) {
        ViewHolder<V> viewHolder = findViewHolderById(id);
        return viewHolder == null ? null : viewHolder.view;
    }

    /**
     * @deprecated use {@link #setView(int, Object)} instead
     */
    @Deprecated
    public final void setView(V view) {
        setView(VisumView.VIEW_ID_DEFAULT, view);
    }

    /**
     * @deprecated use {@link #onViewAttached(Object)} instead
     */
    @Deprecated
    protected void onViewAttached() {}

    /**
     * @deprecated use {@link #onViewDetached(Object)} instead
     */
    @Deprecated
    protected void onViewDetached() {}

    /**
     * @deprecated use {@link #findViewById(int)} instead
     */
    @Deprecated
    @Nullable
    public final V view() {
        return findViewById(VisumView.VIEW_ID_DEFAULT);
    }

}
