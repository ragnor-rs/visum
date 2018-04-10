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
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import org.mockito.Mockito;

import io.reist.visum.presenter.TestPresenter;

/**
 * Created by defuera on 22/06/2016.
 */
public abstract class BaseTestVisumActivity
        extends VisumActivity<TestPresenter>
        implements VisumConfigurableResultReceiver {

    public static final int CONTAINER_ID = 1;

    private static final int REQUEST_CODE = 1;

    private TestPresenter presenter;

    private boolean changingConfigurations;

    private final VisumResultReceiver dummy = Mockito.mock(VisumResultReceiver.class);

    public BaseTestVisumActivity(int viewId) {
        super(viewId);
    }

    @Override
    protected int getLayoutRes() {
        return 0;
    }

    @Override
    public TestPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(TestPresenter presenter) {
        this.presenter = presenter;
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        setTheme(android.support.v7.appcompat.R.style.Theme_AppCompat);

        FrameLayout container = new FrameLayout(this);
        container.setId(CONTAINER_ID);
        setContentView(container);

    }

    @Override
    public void init(Context context, Bundle savedInstanceState) {
        super.init(context, savedInstanceState);
        changingConfigurations = false;
    }

    @Override
    public boolean isChangingConfigurations() {
        return changingConfigurations;
    }

    @Override
    public void setChangingConfigurations(boolean changingConfigurations) {
        this.changingConfigurations = changingConfigurations;
    }

    @Override
    public void startActivityForResult() {
        startActivityForResult(new Intent(this, VisumViewTest.ChildActivity.class), REQUEST_CODE);
    }

    @Override
    public VisumResultReceiver getDummy() {
        return dummy;
    }

    @Override
    public void onActivityResult() {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dummy.onActivityResult();
    }

    @Override
    void internalAttachPresenter() {
        super.internalAttachPresenter();
        dummy.attachPresenter();
    }

    @Override
    public void inject(@NonNull Object component) {
        ((VisumViewTest.TestSubComponent) component).inject(this);
    }

}
