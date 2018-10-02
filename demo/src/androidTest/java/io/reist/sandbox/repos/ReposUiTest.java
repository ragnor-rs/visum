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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
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
    public void testRepo() {

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

        onView(withId(R.id.save)).perform(closeSoftKeyboard(), click());

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
