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

package io.reist.visum.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.VisumBasePresenter;

/**
 * Extend your activities with this class to take advantage of Visum MVP.
 * <p>
 * Created by Defuera on 29/01/16.
 */
public abstract class VisumActivity<P extends VisumBasePresenter>
        extends AppCompatActivity
        implements VisumView<P> {

    private final VisumViewHelper<P> helper;

    /**
     * Used to ensure that {@link #attachPresenter()} is called before
     * {@link #onActivityResult(int, int, Intent)}
     */
    private boolean presenterAttached;


    public VisumActivity() {
        this.helper = new VisumViewHelper<>(new VisumClientHelper<>(this));
    }

    //region VisumClient implementation

    @Override
    public final void onStartClient() {
        helper.onCreate();
    }

    @NonNull
    @Override
    public final ComponentCache getComponentCache() {
        return helper.getComponentCache();
    }

    @Override
    public final void onStopClient() {
        helper.onDestroy(isChangingConfigurations());
    }

    @NonNull
    public final Context getContext() {
        return this;
    }

    //endregion


    //region VisumView implementation

    @Override
    @CallSuper
    public void attachPresenter() {
        helper.attachPresenter();
        presenterAttached = true;
    }

    @Override
    @CallSuper
    public void detachPresenter() {
        helper.detachPresenter();
        presenterAttached = false;
    }

    //endregion


    //region Activity implementation

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onStartClient();
        setContentView(getLayoutRes());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!presenterAttached) {
            attachPresenter();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!presenterAttached) {
            attachPresenter();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenterAttached) {
            detachPresenter();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onStopClient();
    }

    /**
     * The client code must call this implementation via
     * super.onActivityResult(requestCode, resultCode, data) before accessing a presenter.
     */
    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!presenterAttached) {
            attachPresenter();
        }
    }

    //endregion


    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public int getViewId() {
        return DEFAULT_ID;
    }
}
