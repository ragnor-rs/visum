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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.SingleVisumPresenter;
import io.reist.visum.presenter.VisumPresenter;

import static io.reist.visum.view.VisumFragmentUtils.attachPresenterInChildFragments;
import static io.reist.visum.view.VisumFragmentUtils.detachPresenterInChildFragments;

/**
 * Extend your dialog fragments with this class to take advantage of Visum MVP.
 */
@SuppressWarnings("unused")
public abstract class VisumDialogFragment<P extends VisumPresenter>
        extends DialogFragment
        implements VisumView<P> {

    private final VisumViewHelper<P> helper;

    /**
     * Used to ensure that {@link #attachPresenter()} and {@link #detachPresenter()} is called once
     * {@link #onActivityResult(int, int, Intent)}
     */
    private boolean presenterAttached = false;

    public VisumDialogFragment() {
        this(SingleVisumPresenter.VIEW_ID_DEFAULT);
    }

    // todo add javadoc for viewId
    public VisumDialogFragment(int viewId) {
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
        presenterAttached = true;
    }

    @Override
    @CallSuper
    public void detachPresenter() {
        helper.detachPresenter();
        presenterAttached = false;
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
        if (!presenterAttached) {
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
    public void onResume() {
        super.onResume();
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
    }

    //endregion


    @LayoutRes
    protected abstract int getLayoutRes();

}
