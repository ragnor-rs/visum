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

import org.junit.Assert;

import io.reist.visum.view.VisumView;
import rx.Single;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Reist on 07.06.16.
 */
public class PresenterAssert {

    public interface AssertTestPresenter {
        void assertPresenterAttached(int viewId, VisumView view);
        void assertPresenterDetached(int viewId, VisumView view);
    }

    public static void assertPresenterDetached(AssertTestPresenter presenter, int viewId, VisumView view) {
        presenter.assertPresenterDetached(viewId, view);
    }

    public static void assertPresenterAttached(AssertTestPresenter presenter, int viewId, VisumView view) {
        presenter.assertPresenterAttached(viewId, view);
    }

    public static void assertViewSubscribe(int viewId, TestPresenter presenter, boolean expected) {
        Subscription subscription = presenter.subscribe(viewId, Single.just(true), new Action1<Boolean>() {

            @Override
            public void call(Boolean aBoolean) {
            }

        });
        Assert.assertEquals(expected, subscription != null);
    }

    public static void assertGlobalSubscribe(TestPresenter presenter, boolean expected) {
        try {
            presenter.subscribe(Single.just(true), new ViewNotifier<VisumView, Boolean>() {

                @Override
                public void notifyCompleted(VisumView visumView) {}

                @Override
                public void notifyResult(VisumView view, Boolean aBoolean) {}

                @Override
                public void notifyError(VisumView view, Throwable e) {}

            });
            Assert.assertTrue("subscribe() should work here", expected);
        } catch (Exception e) {
            Assert.assertFalse("subscribe() shouldn't work here", expected);
        }
    }

}
