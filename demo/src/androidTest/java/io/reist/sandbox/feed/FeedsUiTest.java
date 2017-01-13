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

package io.reist.sandbox.feed;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reist.sandbox.R;
import io.reist.sandbox.app.view.MainActivity;
import io.reist.sandbox.core.ActivityInstrumentationTestCase;
import io.reist.sandbox.core.TestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by 4xes on 9/11/16.
 */
@RunWith(AndroidJUnit4.class)
public class FeedsUiTest extends ActivityInstrumentationTestCase<MainActivity> {

    MainActivity mMainActivity;

    public FeedsUiTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
    }

    @Test
    public void testLike() throws Throwable {

        // open sidebar
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // go to feed section
        onView(allOf(isDescendantOfA(withId(R.id.nav_view)), withText(R.string.menu_feed)))
                .perform(click());

        onView(isRoot())
                .perform(TestUtils.waitId(R.id.post_title, TestUtils.ACTION_TIMEOUT));

        // click on post
        onView(withId(R.id.feed_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(isRoot())
                .perform(TestUtils.waitId(R.id.post_detail_container, TestUtils.ACTION_TIMEOUT));

        onView(withId(R.id.post_detail_title)).check(matches(isDisplayed()));
        onView(withId(R.id.post_detail_body)).check(matches(isDisplayed()));
    }


}
