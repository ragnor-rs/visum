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

import io.reist.visum.view.VisumView;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * A MVP presenter for handling only one view.
 *
 * {@link SingleViewVisumPresenter} is wrapper for {@link VisumPresenter};
 * views. If view is removed then all subscription will be stopped.
 *
 * Created by 4xes on 23/11/16.
 *
 * @param <V> a type of view to be handled
 */
@SuppressWarnings({"unused"})
public abstract class SingleViewVisumPresenter<V extends VisumView> extends VisumPresenter<V>{

    public static final int VIEW_ID_DEFAULT = 0;

    public final void setView(V view) {
        setView(VIEW_ID_DEFAULT, view);
    }

    @Override
    protected void onViewAttached(int id, @NonNull V view) {
        onViewAttached();
    }

    @Override
    protected void onViewDetached(int id, @NonNull V view) {
        onViewDetached();
    }

    protected void onViewAttached() {}

    protected void onViewDetached() {}

    @NonNull
    public final V view() {
        return view(VIEW_ID_DEFAULT);
    }

    public final <T> Subscription subscribe(Observable<T> observable, Observer<? super T> observer) {
        return subscribe(VIEW_ID_DEFAULT, observable, observer);
    }

    public final <T> Subscription subscribe(Single<T> single, Action1<T> action) {
        return subscribe(VIEW_ID_DEFAULT, single, action);
    }

    public final <T> Subscription subscribe(Single<T> single, SingleSubscriber<T> subscriber) {
        return subscribe(VIEW_ID_DEFAULT, single, subscriber);
    }

    public final boolean hasViewSubscriptions() {
        return hasSubscriptions(VIEW_ID_DEFAULT);
    }


}
