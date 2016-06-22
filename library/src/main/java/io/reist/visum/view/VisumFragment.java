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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.VisumPresenter;

import static io.reist.visum.view.VisumFragmentUtils.attachPresenterInChildFragments;
import static io.reist.visum.view.VisumFragmentUtils.detachPresenterInChildFragments;

/**
 * A {@link Fragment}-based implementation of {@link VisumView}
 *
 * @param <P> - subclass of VisumPresenter
 */
public abstract class VisumFragment<P extends VisumPresenter>
        extends Fragment
        implements VisumView<P> {

    private final VisumViewHelper<P> helper;

    /**
     * Used to ensure that {@link #attachPresenter()} is called before
     * {@link #onActivityResult(int, int, Intent)}
     */
    private boolean presenterAttachedOnActivityResult;

    /**
     * @deprecated use {@link #VisumFragment(int)} instead
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public VisumFragment() {
        this(VisumPresenter.VIEW_ID_DEFAULT);
    }

    // todo add javadoc for viewId
    public VisumFragment(int viewId) {
        this.helper = new VisumViewHelper<>(viewId, new VisumClientHelper<>(this));
    }


    //region VisumClient implementation

    @Override
    @CallSuper
    public void onStartClient() {
        helper.onCreate();
    }

    @NonNull
    @Override
    @CallSuper
    public ComponentCache getComponentCache() {
        return helper.getComponentCache();
    }

    @Override
    @CallSuper
    public void onStopClient() {
        helper.onDestroy(getActivity().isChangingConfigurations());
    }

    //endregion


    //region VisumView implementation

    @Override
    @CallSuper
    public void attachPresenter() {
        helper.attachPresenter();
    }

    @Override
    @CallSuper
    public void detachPresenter() {
        helper.detachPresenter();
        presenterAttachedOnActivityResult = false;
    }

    //endregion


    //region Fragment implementation

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onStartClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!presenterAttachedOnActivityResult) {
            attachPresenter();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        if (hidden) {
            detachPresenter();
            detachPresenterInChildFragments(childFragmentManager);
        } else {
            attachPresenter();
            attachPresenterInChildFragments(childFragmentManager);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detachPresenter();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        attachPresenter();
        presenterAttachedOnActivityResult = true;
    }

    //endregion


    /**
     * @return  a name used to identify this fragment in the back-stack
     *
     * @see VisumFragmentManager#replace(FragmentManager, Fragment, VisumFragment, int, boolean, boolean)
     */
    public final String getName() {
        return getClass().getName();
    }

    @LayoutRes
    protected abstract int getLayoutRes();

}
