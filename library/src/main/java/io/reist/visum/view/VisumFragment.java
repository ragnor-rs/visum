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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.ContextThemeWrapper;
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
     * Used to ensure that {@link #attachPresenter()} and {@link #detachPresenter()} is called once
     * {@link #onActivityResult(int, int, Intent)}
     */
    private boolean presenterAttached = false;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int customTheme = getCustomTheme();
        if (customTheme != 0) {
            // create ContextThemeWrapper from the original Activity Context with the custom theme
            final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), customTheme);

            // clone the inflater using the ContextThemeWrapper
            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
            return localInflater.inflate(getLayoutRes(), container, false);
        } else {
            return inflater.inflate(getLayoutRes(), container, false);
        }

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


    /**
     * @return a name used to identify this fragment in the back-stack
     * @see VisumFragmentManager#replace(FragmentManager, Fragment, VisumFragment, int, boolean, boolean)
     */
    public String getName() {
        return getClass().getName();
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @StyleRes
    protected int getCustomTheme(){
        return 0;
    }

}
