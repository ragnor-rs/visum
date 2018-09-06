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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.reist.visum.VisumTest;
import io.reist.visum.view.BaseTestView;
import io.reist.visum.view.VisumView;
import rx.functions.Func0;

import static io.reist.visum.presenter.PresenterAssert.assertGlobalSubscribe;
import static io.reist.visum.presenter.PresenterAssert.assertViewSubscribe;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Reist on 22.08.16.
 */
public class SubscriptionTest extends VisumTest<BaseTestView> {

    private static final int VIEW_ID = 1;

    private TestPresenter presenter;

    @Before
    public void setUp() {

        presenter = new TestPresenter() {

            @Override
            public void onStart() {

                super.onStart();

                assertViewSubscribe(VIEW_ID, this, false);
                assertGlobalSubscribe(this, true);

                assertTrue(presenter.hasSubscriptions());
                assertFalse(presenter.hasSubscriptions(VIEW_ID));

            }

            @Override
            protected void onViewAttached(int id, @NonNull VisumView view) {

                super.onViewAttached(id, view);

                assertViewSubscribe(VIEW_ID, this, true);
                assertGlobalSubscribe(this, true);

                assertTrue(presenter.hasSubscriptions());
                assertTrue(presenter.hasSubscriptions(VIEW_ID));

            }

            @Override
            protected void onViewDetached(int id, @NonNull VisumView view) {

                super.onViewDetached(id, view);

                assertViewSubscribe(VIEW_ID, this, false);
                assertGlobalSubscribe(this, true);

                assertTrue(presenter.hasSubscriptions());
                assertFalse(presenter.hasSubscriptions(VIEW_ID));

            }

            @Override
            public void onStop() {

                super.onStop();

                assertViewSubscribe(VIEW_ID, this, false);
                assertGlobalSubscribe(this, false);

                assertFalse(presenter.hasSubscriptions());
                assertFalse(presenter.hasSubscriptions(VIEW_ID));

            }

        };

        setUp(
                TestComponent::new,
                BaseTestView.class
        );

    }

    @After
    public void finish() {
        presenter = null;
    }

    @Test
    public void testSubscriptions() {
        BaseTestView view = getClient();
        view.attachPresenter();
        view.detachPresenter();
    }

    @Override
    protected BaseTestView createClient() {
        return new BaseTestView(getComponentCache(), presenter, VIEW_ID) {};
    }

    private static class TestComponent {}

}
