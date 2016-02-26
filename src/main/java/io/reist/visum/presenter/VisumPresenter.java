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

import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Reist on 10/15/15.
 * <p>
 * VisumPresenter is a class that handles attachment and detachment of a {@link io.reist.visum.view.VisumView}.
 * </p>
 * It's also empowered with rx steroids, which makes it handle your subscriptions
 * making you not to worry about android **Views** lifecycle.
 * All your rx subscriptions are to be unsubscribed on view detached.
 * Use {@link VisumPresenter#subscribe(Observable, Observer)} method to gain this benifits.
 *
 * @param <V> View to be handled
 */
public abstract class VisumPresenter<V> {

    private CompositeSubscription subscriptions;
    private V view;

    public final void setView(V view) {
        if (view == null) {
            if (subscriptions != null) {
                subscriptions.unsubscribe();
            }

            if (this.view != null) {
                onViewDetached();
            }

            this.view = null;
        } else {
            if (this.view != null) {
                setView(null);
            }

            this.view = view;
            subscriptions = new CompositeSubscription();
            onViewAttached();
        }
    }

    protected final <T> void subscribe(Observable<T> observable, Observer<? super T> observer) {
        subscriptions.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(observer)
        );
    }

    protected final <T> void subscribe(Single<T> single, Action1<T> observer) {
        subscriptions.add(
                single
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer)
        );
    }

    protected abstract void onViewAttached();

    protected void onViewDetached() {
    }

    public final V view() {
        return view;
    }

}
