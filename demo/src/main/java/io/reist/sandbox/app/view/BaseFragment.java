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

package io.reist.sandbox.app.view;

import android.support.annotation.LayoutRes;

import io.reist.visum.presenter.VisumPresenter;
import io.reist.visum.view.VisumFragment;
import io.reist.vui.view.VisumCompositeFragment;

/**
 * Created by defuera on 03/02/2016.
 */
public abstract class BaseFragment<P extends VisumPresenter> extends VisumCompositeFragment<P> {

    private final int layoutResId;

    public BaseFragment(@LayoutRes int layoutResId) {
        super();
        this.layoutResId = layoutResId;
    }

    public BaseFragment(int viewId, @LayoutRes int layoutResId) {
        super(viewId);
        this.layoutResId = layoutResId;
    }

    protected FragmentController getFragmentController() {
        Object a = getActivity();
        if (a instanceof FragmentController) {
            return (FragmentController) a;
        } else {
            throw new IllegalArgumentException("Can't find " + FragmentController.class.getSimpleName());
        }
    }

    public interface FragmentController {
        void showFragment(VisumFragment fragment, boolean remove);
    }

    @Override
    protected int getLayoutRes() {
        return layoutResId;
    }

}
