/*
 * Copyright (C) 2017 Renat Sarymsakov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.reist.visum.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.SingleViewPresenter;
import io.reist.visum.presenter.VisumPresenter;

import static io.reist.visum.view.VisumFragmentUtils.attachPresenterInChildFragments;
import static io.reist.visum.view.VisumFragmentUtils.detachPresenterInChildFragments;

/**
 * Extend your bottom sheet dialog fragments with this class to take advantage of Visum MVP.
 */
@SuppressWarnings("unused")
public abstract class VisumBottomSheetDialogFragment<P extends VisumPresenter>
        extends BottomSheetDialogFragment
        implements VisumView<P> {

    private final VisumViewHelper<P> helper;

    /**
     * Used to ensure that {@link #attachPresenter()} and {@link #detachPresenter()} is called once
     * {@link #onActivityResult(int, int, Intent)}
     */
    private boolean presenterAttached = false;

    public VisumBottomSheetDialogFragment() {
        this(SingleViewPresenter.DEFAULT_VIEW_ID);
    }

    // todo add javadoc for viewId
    public VisumBottomSheetDialogFragment(int viewId) {
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
        if (!getActivity().isChangingConfigurations()) {
            onStartClient();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!getActivity().isChangingConfigurations()) {
            if (!presenterAttached) {
                attachPresenter();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        if (hidden && !presenterAttached || !hidden && presenterAttached) {
            return;
        }

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
        if (!getActivity().isChangingConfigurations()) {
            if (presenterAttached) {
                detachPresenter();
            }
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (!getActivity().isChangingConfigurations()) {
            onStopClient();
        }
        super.onDestroy();
    }

    /**
     * The client code must call this implementation via
     * super.onActivityResult(requestCode, resultCode, data) before accessing a presenter.
     */
    @CallSuper
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!presenterAttached) {
            attachPresenter();
        }
    }

    //endregion


    @LayoutRes
    protected abstract int getLayoutRes();

}
