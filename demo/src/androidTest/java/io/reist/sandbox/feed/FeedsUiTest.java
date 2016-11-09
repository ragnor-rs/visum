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

        onView(withId(R.id.post_detail_title));
        onView(withId(R.id.post_detail_body));

        onView(withId(R.id.post_detail_title)).check(matches(withText("sunt aut facere repellat provident occaecati excepturi optio reprehenderit")));
    }


}
