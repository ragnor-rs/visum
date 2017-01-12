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

package io.reist.sandbox.core;

import android.support.annotation.IdRes;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.view.View;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;

import java.util.concurrent.TimeoutException;

import rx.functions.Func1;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by m039 on 11/27/15.
 */
public class TestUtils {

    /**
     * Should take into consideration delays during network operations
     */
    public static final int ACTION_TIMEOUT = 30000;

    public static ViewAction waitId(final @IdRes int viewId, final long timeout) {
        return wait(withId(viewId), timeout);
    }

    public static ViewAction waitId(final @IdRes int viewId, final long timeout, final Func1<View, Boolean> condition) {
        return wait(withId(viewId), timeout, condition);
    }

    public static ViewAction clickOnId(final @IdRes int resId) {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with " + resId;
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.findViewById(resId).performClick();
            }

        };
    }

    public static ViewAction wait(final Matcher<View> viewMatcher, final long timeout) {
        return wait(viewMatcher, timeout, v -> true);
    }

    public static ViewAction wait(final Matcher<View> viewMatcher, final long timeout, final Func1<View, Boolean> condition) {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return new IsAnything<>();
            }

            @Override
            public String getDescription() {
                return "wait for a view which matches <" + viewMatcher + "> during " + timeout + " millis";
            }

            @Override
            public void perform(final UiController uiController, final View view) {

                uiController.loopMainThreadUntilIdle();

                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + timeout;

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (viewMatcher.matches(child) && condition.call(child)) {
                            return;
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();

            }

        };
    }

}
