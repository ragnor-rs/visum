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

package io.reist.visum.presenter;

import android.support.annotation.NonNull;

import org.mockito.Mockito;

import io.reist.visum.view.VisumView;

/**
 * Created by Reist on 26.05.16.
 */
public class TestPresenter extends VisumPresenter<VisumView> {

    private final TestPresenter dummy = Mockito.mock(TestPresenter.class);

    @Override
    public void onStop() {
        super.onStop();
        dummy.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        dummy.onStart();
    }

    @Override
    protected void onViewAttached(int id, @NonNull VisumView view) {
        dummy.onViewAttached(id, view);
    }

    @Override
    protected void onViewDetached(int id, @NonNull VisumView view) {
        dummy.onViewDetached(id, view);
    }

    public TestPresenter getDummy() {
        return dummy;
    }

}
