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
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.model.ViewModel;
import io.reist.visum.presenter.SingleViewPresenter;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Extend your {@link FrameLayout}s with this class to take advantage of Visum MVP.
 *
 * Prevents client code from overriding {@link #onDetachedFromWindow()}.
 * This is to minimize bugs regarding view attachment. For proper initialization / cleanup, use
 * {@link #attachPresenter()} and {@link #detachPresenter()}.
 *
 * Created by Defuera on 29/01/16.
 */
@SuppressWarnings("unused")
public abstract class VisumWidget<P extends VisumPresenter, VM extends ViewModel>
        extends FrameLayout
        implements VisumView<P>, ViewModelWidget<VM> {

    private VM viewModel;

    private final VisumViewHelper<P> helper;

    public VisumWidget(int viewId, Context context) {
        super(context);
        inflate();
        this.helper = new VisumViewHelper<>(viewId, new VisumClientHelper<>(this));
    }

    public VisumWidget(int viewId, Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate();
        this.helper = new VisumViewHelper<>(viewId, new VisumClientHelper<>(this));
    }

    public VisumWidget(Context context) {
        this(SingleViewPresenter.DEFAULT_VIEW_ID, context);
    }

    public VisumWidget(Context context, AttributeSet attrs) {
        this(SingleViewPresenter.DEFAULT_VIEW_ID, context, attrs);
    }

    @Override
    public final void onStartClient() {
        helper.onCreate();
    }

    @Override
    public final ComponentCache getComponentCache() {
        return isInEditMode() ? null : helper.getComponentCache();
    }

    @Override
    public final void onStopClient() {
        helper.onDestroy(false);
    }

    @Override
    @CallSuper
    public final void attachPresenter() {
        bindUiElements();
        helper.attachPresenter();
    }

    @Override
    @CallSuper
    public final void detachPresenter() {
        helper.detachPresenter();
        unbindUiElements();
    }

    @Override
    protected final void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            onStartClient();
        }
        attachPresenter();
        bindUiElements();
    }

    @Override
    protected final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        detachPresenter();
        onStopClient();
        unbindUiElements();
    }

    protected final void inflate() {
        inflate(getContext(), getLayoutRes(), this);
        bindUiElements();
    }

    @Override
    public final void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!isInEditMode()) {
            super.addView(child, index, params);
        }
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @CallSuper
    @Override
    public void init(@Nullable AttributeSet attributeSet) {}

    @Override
    public final void rebindUiElements() {
        unbindUiElements();
        bindUiElements();
    }

    @Override
    public void bindUiElements() {}

    @Override
    public void unbindUiElements() {}

    protected abstract void bindViews(View view);

    @Override
    public final VM getViewModel() {
        return viewModel;
    }

    @CallSuper
    @Override
    public void bindViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
        bindUiElements();
    }

}
