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

package io.reist.sandbox.result.presenter;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.result.view.ResultView;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Reist on 07.06.16.
 */
@Singleton
public class ResultPresenter extends VisumPresenter<ResultView> {

    private static final String TAG = ResultPresenter.class.getSimpleName();

    public void receiveResult() {
        Log.i(TAG, "Presenter received a result successfully");
    }

    @Inject
    public ResultPresenter() {}

}
