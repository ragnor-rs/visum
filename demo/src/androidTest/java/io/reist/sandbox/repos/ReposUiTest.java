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

package io.reist.sandbox.repos;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import io.reist.sandbox.R;
import io.reist.sandbox.app.view.MainActivity;
import io.reist.sandbox.core.ActivityInstrumentationTestCase;
import io.reist.sandbox.core.TestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by m039 on 11/27/15.
 */
@RunWith(AndroidJUnit4.class)
public class ReposUiTest extends ActivityInstrumentationTestCase<MainActivity> {

    public ReposUiTest() {
        super(MainActivity.class);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        getActivity();
    }

    @Test
    public void testRepo() throws InterruptedException {

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        onView(allOf(isDescendantOfA(withId(R.id.nav_view)), withText(R.string.menu_repos)))
                .perform(click());

        onView(isRoot())
                .perform(TestUtils.waitId(R.id.daggertest_repo_item_text_view, TestUtils.ACTION_TIMEOUT));

        onView(withId(R.id.daggertest_repo_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(isRoot())
                .perform(TestUtils.waitId(R.id.repo_name, TestUtils.ACTION_TIMEOUT, v -> {
                    final CharSequence text = ((TextView) v).getText();
                    return !TextUtils.isEmpty(text);
                }));

        String generatedRepoName = generateRepoName();

        onView(withId(R.id.repo_name))
                .perform(clearText())
                .perform(typeText(generatedRepoName));

        onView(withId(R.id.save)).perform(click());

        onView(withContentDescription(android.support.v7.appcompat.R.string.abc_action_bar_up_description))
                .perform(click());

        onView(isRoot())
                .perform(TestUtils.wait(withText(generatedRepoName), TestUtils.ACTION_TIMEOUT));

        onView(withText(generatedRepoName)).check(matches(isDisplayed()));

    }

    public static String generateRepoName() {
        return "cracky" + (new Random().nextInt(10000));
    }

}
