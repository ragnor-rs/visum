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

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Extend your activities with this class to take advantage of Visum MVP.
 *
 * Created by Defuera on 29/01/16.
 */
public abstract class VisumActivity<P extends VisumPresenter>
        extends AppCompatActivity
        implements VisumView<P> {

    private final VisumViewHelper helper;

    /**
     * @deprecated use {@link #VisumActivity(int)} instead
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public VisumActivity() {
        this(VisumPresenter.VIEW_ID_DEFAULT);
    }

    public VisumActivity(int viewId) {
        this.helper = new VisumViewHelper(viewId, new VisumClientHelper(this));
    }


    //region VisumClient implementation

    @Override
    public final Long getComponentId() {
        return helper.getComponentId();
    }

    @Override
    public final void setComponentId(Long componentId) {
        helper.setComponentId(componentId);
    }

    @Override
    public final Object getComponent() {
        return helper.getComponent();
    }

    @Override
    public final ComponentCache getComponentCache() {
        return helper.getComponentCache(this);
    }

    @Override
    public void onInvalidateComponent() {
        helper.onInvalidateComponent();
    }

    //endregion


    //region VisumView implementation

    @Override
    public void attachPresenter() {
        helper.attachPresenter();
    }

    @Override
    public void detachPresenter() {
        helper.detachPresenter();
    }

    @Override
    public int getViewId() {
        return helper.getViewId();
    }

    //endregion


    //region Activity implementation

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper.onCreate();
        helper.onRestoreInstanceState(savedInstanceState);
        setContentView(getLayoutRes());
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        helper.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        helper.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.onDestroy();
    }

    //endregion


    @LayoutRes
    protected abstract int getLayoutRes();

}
