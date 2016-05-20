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
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reist.visum.ComponentCache;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Extend your dialog fragments with this class to take advantage of Visum MVP.
 */
@SuppressWarnings("unused")
public abstract class VisumDialogFragment<P extends VisumPresenter>
        extends DialogFragment
        implements VisumView<P> {

    private final VisumViewHelper<VisumDialogFragment<P>> viewHelper;

    /**
     * @deprecated use {@link #VisumDialogFragment(int)} instead
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public VisumDialogFragment() {
        this(VisumPresenter.VIEW_ID_DEFAULT);
    }

    public VisumDialogFragment(int viewId) {
        this.viewHelper = new VisumViewHelper<>(viewId, this);
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
        return viewHelper.getComponentCache(getActivity());
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


    //region Fragment implementation

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewHelper.onCreate();
        viewHelper.onRestoreInstanceState(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHelper.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            viewHelper.detachPresenter();
        } else {
            viewHelper.attachPresenter();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewHelper.onPause();
        viewHelper.onDestroy();
    }

    //endregion


    @LayoutRes
    protected abstract int getLayoutRes();

}
