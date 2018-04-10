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

package io.reist.sandbox.result.view;

import android.content.Intent;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import butterknife.OnClick;
import io.reist.sandbox.R;
import io.reist.sandbox.result.ResultComponent;
import io.reist.sandbox.result.presenter.ResultPresenter;
import io.reist.visum.view.VisumActivity;

/**
 * This activity demonstrates how a result received from another activity can be passed directly
 * to a {@link io.reist.visum.presenter.VisumPresenter}. This was made possible by performing
 * dependency injection in {@link VisumActivity#onActivityResult(int, int, Intent)}. Among other
 * dependencies, a presenter is injected directly into the activity. Since then, you can call any
 * of presenter's methods. In this example, the method is {@link ResultPresenter#receiveResult()}.
 *
 * Created by Reist on 07.06.16.
 */
public class ResultActivity extends VisumActivity<ResultPresenter> implements ResultView {

    private static final int REQUEST_CODE = 1;

    @Inject
    ResultPresenter presenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_result;
    }

    @Override
    public ResultPresenter getPresenter() {
        return presenter;
    }

    @OnClick(R.id.start_and_receive_btn)
    void onStartAndReceiveClicked() {
        startActivityForResult(new Intent(this, SubActivity.class), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.receiveResult();
    }

    @Override
    public void inject(@NonNull Object component) {
        ((ResultComponent) component).inject(this);
    }

}
