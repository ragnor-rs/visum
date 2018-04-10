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

import android.content.Intent;
import android.support.annotation.NonNull;

import org.mockito.Mockito;

import io.reist.visum.R;
import io.reist.visum.presenter.TestPresenter;

/**
 * Created by Reist on 16.06.16.
 */
public abstract class BaseTestVisumDialogFragment
        extends VisumDialogFragment<TestPresenter>
        implements VisumResultReceiver {

    private static final int REQUEST_CODE = 1;

    private final VisumResultReceiver dummy = Mockito.mock(VisumResultReceiver.class);

    private TestPresenter presenter;

    public BaseTestVisumDialogFragment(int viewId) {
        super(viewId);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.test;
    }

    @Override
    public TestPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(TestPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void startActivityForResult() {
        startActivityForResult(new Intent(getActivity(), VisumViewTest.ChildActivity.class), REQUEST_CODE);
    }

    @Override
    public VisumResultReceiver getDummy() {
        return dummy;
    }

    @Override
    public void onActivityResult() {}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dummy.onActivityResult();
    }

    @Override
    public void attachPresenter() {
        super.attachPresenter();
        dummy.attachPresenter();
    }

    @Override
    public void inject(@NonNull Object component) {
        ((VisumViewTest.TestSubComponent) component).inject(this);
    }

}
