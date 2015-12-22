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

import android.content.Context;

import io.reist.visum.view.BaseView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Reist on 10/15/15.
 */
public abstract class BasePresenter<V extends BaseView> {

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

    protected abstract void onViewAttached();

    protected void onViewDetached() {}

    public final V view() {
        return view;
    }

    public final Context getContext() {
        return view == null ? null : view.context();
    }

}
