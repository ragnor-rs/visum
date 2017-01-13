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
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.SingleViewPresenter;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Extend your {@link FrameLayout}s with this class to take advantage of Visum MVP.
 *
 * Created by Defuera on 29/01/16.
 */
@SuppressWarnings("unused")
public abstract class VisumWidget<P extends VisumPresenter>
        extends FrameLayout
        implements VisumView<P> {

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
        this(SingleViewPresenter.DEFAULT_VIEW_ID, context);
    }

    //region VisumClient implementation

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
    }

    //endregion


    //region View implementation

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            onStartClient();
        }
        attachPresenter();
    }

    protected void inflate() {
        inflate(getContext(), getLayoutRes(), this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        detachPresenter();
        onStopClient();
    }

    //endregion


    @LayoutRes
    protected abstract int getLayoutRes();

}
