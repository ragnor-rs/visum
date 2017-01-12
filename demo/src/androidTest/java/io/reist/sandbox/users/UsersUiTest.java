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

package io.reist.sandbox.users;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reist.sandbox.R;
import io.reist.sandbox.app.view.MainActivity;
import io.reist.sandbox.core.ActivityInstrumentationTestCase;
import io.reist.sandbox.core.TestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static io.reist.sandbox.core.TestUtils.clickOnId;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by m039 on 11/20/15.
 */
@RunWith(AndroidJUnit4.class)
public class UsersUiTest extends ActivityInstrumentationTestCase<MainActivity> {

    MainActivity mMainActivity;

    public UsersUiTest() {
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

        // go to users section
        onView(allOf(isDescendantOfA(withId(R.id.nav_view)), withText(R.string.menu_users)))
                .perform(click());

        onView(isRoot())
                .perform(TestUtils.waitId(R.id.name, TestUtils.ACTION_TIMEOUT));

        // click on user
        onView(withId(R.id.recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(isRoot())
                .perform(TestUtils.waitId(R.id.like, TestUtils.ACTION_TIMEOUT));

        // is first repo liked?
        boolean isLiked = isRepoLiked(R.id.recycler, 0);

        Log.i(UsersUiTest.class.getName(), "isRepoLiked = " + isLiked);

        // click on like button
        onView(withId(R.id.recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickOnId(R.id.like)));

        onView(isRoot())
                .perform(TestUtils.waitId(R.id.like, TestUtils.ACTION_TIMEOUT, v -> !isLiked == isRepoLiked(R.id.recycler, 0)));

        // click on like button (should be an opposite of first button state)
        onView(withId(R.id.recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickOnId(R.id.like)));

        onView(isRoot())
                .perform(TestUtils.waitId(R.id.like, TestUtils.ACTION_TIMEOUT, v -> isLiked == isRepoLiked(R.id.recycler, 0)));

    }

    boolean isRepoLiked(@IdRes int recyclerViewId, int position) {

        Activity activity = mMainActivity;

        TextView like = (TextView) ((RecyclerView) activity.findViewById(recyclerViewId))
                .findViewHolderForAdapterPosition(position)
                .itemView
                .findViewById(R.id.like);

        final String label = like.getText().toString();

        if (label.equalsIgnoreCase(activity.getString(R.string.repo_button_unlike))) {
            return true;
        } else if (label.equalsIgnoreCase(activity.getString(R.string.repo_button_like))) {
            return false;
        } else {
            fail("Like button label: " + label);
            return false;
        }

    }

}
