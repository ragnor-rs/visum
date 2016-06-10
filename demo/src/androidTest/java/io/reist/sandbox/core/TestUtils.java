/*
 * Copyright (c) 2015  Zvooq LTD.
 * Authors: Renat Sarymsakov, Dmitriy Mozgin, Denis Volyntsev.
 *
 * This file is part of MVP-Sandbox.
 *
 * MVP-Sandbox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MVP-Sandbox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MVP-Sandbox.  If not, see <http://www.gnu.org/licenses/>.
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
