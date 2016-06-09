/*
 * Copyright (c) 2016  Zvooq LTD.
 * Authors: Renat Sarymsakov, Dmitriy Mozgin, Denis Volyntsev.
 *
 * This file is part of MVP-Sandbox.
 *
 * MVP-Sandbox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MVP-Sandbox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MVP-Sandbox.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.reist.sandbox.app.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.reist.visum.presenter.VisumPresenter;
import io.reist.visum.view.VisumFragment;

/**
 * Created by defuera on 03/02/2016.
 */
public abstract class BaseFragment<P extends VisumPresenter> extends VisumFragment<P> {

    private final int layoutResId;

    /**
     * @deprecated use {@link #BaseFragment(int, int)} instead
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public BaseFragment(@LayoutRes int layoutResId) {
        this(VisumPresenter.VIEW_ID_DEFAULT, layoutResId);
    }

    public BaseFragment(int viewId, @LayoutRes int layoutResId) {
        super(viewId);
        this.layoutResId = layoutResId;
    }

    protected FragmentController getFragmentController() {
        Object a = getActivity();
        if (a instanceof FragmentController) {
            return (FragmentController) a;
        } else {
            throw new IllegalArgumentException("Can't find " + FragmentController.class.getSimpleName());
        }
    }

    public interface FragmentController {
        void showFragment(VisumFragment fragment, boolean remove);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected int getLayoutRes() {
        return layoutResId;
    }

}
