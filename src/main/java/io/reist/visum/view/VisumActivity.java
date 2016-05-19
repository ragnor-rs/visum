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
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Defuera on 29/01/16.
 */
public abstract class VisumActivity<P extends VisumPresenter>
        extends AppCompatActivity
        implements VisumView<P> {

    private final VisumViewHelper viewHelper;

    /**
     * @deprecated use {@link #VisumActivity(int)} instead
     */
    @Deprecated
    public VisumActivity() {
        this(VisumView.VIEW_ID_DEFAULT);
    }

    public VisumActivity(int viewId) {
        this.viewHelper = new VisumViewHelper(viewId, this);
    }


    //region VisumClient implementation

    @Override
    public final Long getComponentId() {
        return viewHelper.getComponentId();
    }

    @Override
    public final void setComponentId(Long componentId) {
        viewHelper.setComponentId(componentId);
    }

    @Override
    public final Object getComponent() {
        return viewHelper.getComponent();
    }

    @Override
    public final ComponentCache getComponentCache() {
        return viewHelper.getComponentCache(this);
    }

    //endregion


    //region VisumView implementation

    @Override
    public void onInvalidateComponent() {
        viewHelper.onInvalidateComponent();
    }

    @Override
    public void attachPresenter() {
        viewHelper.attachPresenter();
    }

    @Override
    public void detachPresenter() {
        viewHelper.detachPresenter();
    }

    //endregion


    //region Activity implementation

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewHelper.onCreate();
        viewHelper.onRestoreInstanceState(savedInstanceState);
        setContentView(getLayoutRes());
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewHelper.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        viewHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewHelper.onDestroy();
    }

    //endregion


    @LayoutRes
    protected abstract int getLayoutRes();

}
