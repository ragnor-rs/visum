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
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.ContextThemeWrapper;
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
 * A {@link Fragment}-based implementation of {@link VisumView}
 *
 * Prevents client code from overriding standard Android lifecycle methods.
 * This is to minimize bugs regarding view attachment. For proper initialization / cleanup, use
 * {@link #init(Context, Bundle)}, {@link #attachPresenter()} and {@link #detachPresenter()}.
 *
 * @param <P> - subclass of VisumPresenter
 */
public abstract class VisumFragment<P extends VisumPresenter>
        extends Fragment
        implements VisumView<P>, CompositeView {

    private final VisumViewHelper<P> helper;

    /**
     * Used to ensure that {@link #attachPresenter()} and {@link #detachPresenter()} is called once
     * {@link #onActivityResult(int, int, Intent)}
     */
    private boolean presenterAttached = false;

    public VisumFragment() {
        this(SingleViewPresenter.DEFAULT_VIEW_ID);
    }

    public VisumFragment(int viewId) {
        this.helper = new VisumViewHelper<>(viewId, new VisumClientHelper<>(this));
    }

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
        helper.onDestroy(getActivity().isChangingConfigurations());
    }

    @Override
    @CallSuper
    public final void attachPresenter() {
        FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            bindViews(getView());
            helper.attachPresenter();
            presenterAttached = true;
        }
        internalAttachPresenter();
    }

    void internalAttachPresenter() {}

    @Override
    @CallSuper
    public final void detachPresenter() {
        helper.detachPresenter();
        presenterAttached = false;
        unbindUiElements();
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onStartClient();
    }

    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;

        int customTheme = getCustomTheme();

        if (customTheme != 0) {

            // create ContextThemeWrapper from the original Activity Context with the custom theme
            final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), customTheme);

            // clone the inflater using the ContextThemeWrapper
            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
            view = localInflater.inflate(getLayoutRes(), container, false);

        } else {
            view = inflater.inflate(getLayoutRes(), container, false);
        }

        bindViews(view);
        init(getContext(), savedInstanceState);

        return view;

    }

    @Override
    public final void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!presenterAttached) {
            attachPresenter();
        }
    }

    @Override
    public final void onHiddenChanged(boolean hidden) {

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
    public final void onResume() {
        super.onResume();
        if (!presenterAttached) {
            attachPresenter();
        }
    }

    @Override
    public final void onPause() {
        super.onPause();
        if (presenterAttached) {
            detachPresenter();
        }
    }

    @Override
    public final void onDestroyView() {
        super.onDestroyView();
        if (presenterAttached) {
            detachPresenter();
        }
    }

    @Override
    public final void onDestroy() {
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

    @LayoutRes
    protected abstract int getLayoutRes();

    @StyleRes
    protected int getCustomTheme() {
        return 0;
    }

    @CallSuper
    @Override
    public void init(Context context, Bundle savedInstanceState) {}

    @Override
    public final void rebindUiElements() {
        unbindUiElements();
        bindUiElements();
    }

    @Override
    public final void bindUiElements() {
        bindViews(getView());
    }

    @Override
    public void unbindUiElements() {}

    protected void bindViews(View view) {}

}
