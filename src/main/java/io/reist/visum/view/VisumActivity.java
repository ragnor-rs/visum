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

import butterknife.ButterKnife;
import io.reist.visum.ComponentCache;
import io.reist.visum.ComponentCacheProvider;
import io.reist.visum.VisumClient;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Defuera on 29/01/16.
 */
public abstract class VisumActivity<P extends VisumPresenter> extends AppCompatActivity implements VisumView<P>, VisumClient {

    private static final String ARG_STATE_COMPONENT_ID = "ARG_STATE_COMPONENT_ID";

    private Long componentId;
    private boolean stateSaved;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inject(getComponent());

        setContentView(getLayoutRes());
        ButterKnife.bind(this);

        attachPresenter();
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @SuppressWarnings("unchecked")
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!stateSaved) {
            getComponentCache().invalidateComponentFor(this);
        }
        detachPresenter();
    }


    //region VisumClient

    @Override
    public Long getComponentId() {
        return componentId;
    }

    @Override
    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    @Override
    public Object getComponent() {
        if (getComponentCache() != null) {
            return getComponentCache().getComponentFor(this);
        } else {
            return null;
        }
    }

    @Override
    public ComponentCache getComponentCache() {
        ComponentCacheProvider application = (ComponentCacheProvider) getApplicationContext();
        return application.getComponentCache();
    }

    //endregion

    //region VisumView

    @SuppressWarnings("unchecked") //todo setView should be checked call
    @Override
    public void attachPresenter() {
        final P presenter = getPresenter();
        if (presenter != null) {
            presenter.setView(this);
        }
    }

    @SuppressWarnings("unchecked") //todo setView should be type safe call
    @Override
    public void detachPresenter() {
        if (getPresenter() != null)
            getPresenter().setView(null);
    }

    //endregion


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        stateSaved = true;

        Bundle bundle = new Bundle();
        bundle.putLong(ARG_STATE_COMPONENT_ID, componentId);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            componentId = savedInstanceState.getLong(ARG_STATE_COMPONENT_ID);
        }

        stateSaved = false;
    }

}
