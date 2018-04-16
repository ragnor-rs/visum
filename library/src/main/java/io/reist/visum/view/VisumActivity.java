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
import android.support.v7.app.AppCompatActivity;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.SingleViewPresenter;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Extend your activities with this class to take advantage of Visum MVP.
 *
 * Prevents client code from overriding standard Android lifecycle methods.
 * This is to minimize bugs regarding view attachment. For proper initialization / cleanup, use
 * {@link #init(Context, Bundle)}, {@link #attachPresenter()} and {@link #detachPresenter()}.
 *
 * Created by Defuera on 29/01/16.
 */
public abstract class VisumActivity<P extends VisumPresenter>
        extends AppCompatActivity
        implements VisumView<P>, CompositeView {

    private final VisumViewHelper<P> helper;

    /**
     * Used to ensure that {@link #attachPresenter()} is called before
     * {@link #onActivityResult(int, int, Intent)}
     */
    private boolean presenterAttached;

    public VisumActivity() {
        this(SingleViewPresenter.DEFAULT_VIEW_ID);
    }

    public VisumActivity(int viewId) {
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
        helper.onDestroy(isChangingConfigurations());
    }

    @NonNull
    public final Context getContext() {
        return this;
    }

    @Override
    public final void attachPresenter() {
        if (!isFinishing()) {
            bindUiElements();
            helper.attachPresenter();
            presenterAttached = true;
        }
        internalAttachPresenter();
    }

    void internalAttachPresenter() {}

    @Override
    public final void detachPresenter() {
        helper.detachPresenter();
        presenterAttached = false;
        unbindUiElements();
    }

    @Override
<<<<<<< HEAD
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onStartClient();
        setContentView(getLayoutRes());
        bindUiElements();
        init(getContext(), savedInstanceState);
    }

    @Override
    protected final void onResume() {
        super.onResume();
        if (!presenterAttached) {
            attachPresenter();
=======
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (!isChangingConfigurations()) {
            onStartClient();
            if (!presenterAttached) {
                attachPresenter();
            }
>>>>>>> develop
        }

        setContentView(getLayoutRes());

    }

    @Override
<<<<<<< HEAD
    protected final void onPause() {
        super.onPause();
        if (presenterAttached) {
            detachPresenter();
=======
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!presenterAttached) {
            attachPresenter();
        }
    }

    @Override
    public void onDestroy() {

        if (!isChangingConfigurations()) {
            if (presenterAttached) {
                detachPresenter();
            }
            onStopClient();
>>>>>>> develop
        }

<<<<<<< HEAD
    @Override
    protected final void onDestroy() {
=======
>>>>>>> develop
        super.onDestroy();

    }

    /**
     * The client code must call super implementation before accessing the presenter.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!presenterAttached) {
            attachPresenter();
        }
    }

    /**
     * The client code must call super implementation before accessing the presenter.
     */
    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!presenterAttached) {
            attachPresenter();
        }
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @CallSuper
    @Override
    public void init(Context context, Bundle savedInstanceState) {}

    @Override
    public final void rebindUiElements() {
        unbindUiElements();
        bindUiElements();
    }

    @Override
    public void bindUiElements() {}

    @Override
    public void unbindUiElements() {}

}
