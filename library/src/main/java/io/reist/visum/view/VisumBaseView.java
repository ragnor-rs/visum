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

import io.reist.visum.VisumBaseClient;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.SingleViewPresenter;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Reist on 20.05.16.
 */
@SuppressWarnings("unused")
public abstract class VisumBaseView<P extends VisumPresenter>
        extends VisumBaseClient implements VisumView<P> {

    private static final String TAG = VisumBaseView.class.getName();

    private final VisumViewHelper<P> helper;

    public VisumBaseView(Context context) {
        this(SingleViewPresenter.DEFAULT_VIEW_ID, context);
    }

    public VisumBaseView(int viewId, Context context) {
        super(context);
        this.helper = new VisumViewHelper<>(viewId, new VisumClientHelper<>(this));
    }


    //region VisumView implementation

    @Override
    @CallSuper
    public void attachPresenter() {
        onStartClient();
        helper.attachPresenter();
    }

    @Override
    @CallSuper
    public void detachPresenter() {
        helper.detachPresenter();
        onStopClient();
    }

    //endregion


}
