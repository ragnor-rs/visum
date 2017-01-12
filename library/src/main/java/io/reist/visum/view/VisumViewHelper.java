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
import android.support.annotation.NonNull;
import android.util.Log;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.VisumPresenter;

/**
 * A helper class for implementations of {@link VisumView}. It provides callback for
 * typical Android UI components such as {@link android.app.Activity} and
 * {@link android.app.Fragment}.
 * <p>
 * Created by Reist on 19.05.16.
 */
public final class VisumViewHelper<P extends VisumPresenter> {

    private static final String LOG_TAG = VisumViewHelper.class.getSimpleName();

    private final int viewId;
    private final VisumClientHelper<? extends VisumView<P>> helper;

    public VisumViewHelper(int viewId, @NonNull VisumClientHelper<? extends VisumView<P>> helper) {
        this.viewId = viewId;
        this.helper = helper;
    }

    public P getPresenter() {
        return helper.getClient().getPresenter();
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void attachPresenter() {
        VisumView<P> view = helper.getClient();
        VisumPresenter presenter = view.getPresenter();
        if (presenter != null) {
            presenter.setView(viewId, view);
        } else {
            Log.w(LOG_TAG, "presenter is null");
        }
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void detachPresenter() {
        VisumView<P> view = helper.getClient();
        VisumPresenter presenter = view.getPresenter();
        if (presenter != null) {
            presenter.setView(viewId, null);
        } else {
            Log.w(LOG_TAG, "presenter is null");
        }
    }

    public ComponentCache getComponentCache() {
        return helper.getComponentCache();
    }

    public void onCreate() {
        helper.onCreate();
    }

    public void onDestroy(boolean retainComponent) {
        helper.onDestroy(retainComponent);
    }

    @NonNull
    public Context getContext() {
        return helper.getContext();
    }

}
