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

package io.reist.visum.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import io.reist.visum.R;
import io.reist.visum.TestApplication;
import io.reist.visum.VisumImplTest;
import io.reist.visum.presenter.TestPresenter;
import rx.functions.Func1;

import static io.reist.visum.presenter.PresenterAssert.assertPresenterAttached;
import static io.reist.visum.presenter.PresenterAssert.assertPresenterDetached;
import static io.reist.visum.view.ViewAssert.assertPresenterAttachedBeforeOnActivityResult;
import static io.reist.visum.view.ViewAssert.assertPresenterReattached;

/**
 * Created by Reist on 26.05.16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.JELLY_BEAN,
        application = TestApplication.class
)
public class VisumViewTest extends VisumImplTest<VisumViewTest.TestComponent> {

    static final int VIEW_ID = 1;
    static final int CHILD_VIEW_ID = 2;

    /**
     * A presenter from a sub-component. It will be null after the sub-component is removed from the
     * {@link io.reist.visum.ComponentCache}.
     */
    private TestPresenter testPresenter;

    @Override
    protected TestComponent createComponent() {
        return new TestComponent(this);
    }

    @Before
    public void start() {
        setUp();
        getComponentCache().register(
                () -> getComponent().testSubComponent(),
                TestVisumView.class,
                BaseTestVisumFragment.class,
                BaseTestVisumDialogFragment.class,
                BaseTestVisumBottomSheetDialogFragment.class,
                BaseTestVisumActivity.class,
                BaseTestVisumAccountAuthenticatorActivity.class,
                TestVisumWidget.class
        );
    }

    @After
    public void finish() {
        tearDown();
        testPresenter = null;
    }

    @Test
    public void visumBaseView() {

        TestVisumView testView = new TestVisumView(RuntimeEnvironment.application);

        testView.attachPresenter();
        assertPresenterAttached(testPresenter, VIEW_ID, testView);

        testView.detachPresenter();
        assertPresenterDetached(testPresenter, VIEW_ID, testView);

    }

    @Test
    public void visumAccountAuthenticatorActivity() {
        testActivity(TestVisumAccountAuthenticatorActivity.class);
    }

    @Test
    public void visumActivity() {
        testActivity(TestVisumActivity.class);
    }

    @Test
    public void visumActivityWithFragment() {

        testDetachFragmentOnDetachActivity(
                TestVisumActivity.class,
                TestVisumActivity.CONTAINER_ID,
                new TestVisumChildFragment()
        );

        testDetachFragmentOnDetachActivity(
                TestVisumActivity.class,
                TestVisumActivity.CONTAINER_ID,
                new TestVisumChildDialogFragment()
        );

    }

    @Test
    public void visumAccountAuthenticatorActivityWithFragment() {

        testDetachFragmentOnDetachActivity(
                TestVisumAccountAuthenticatorActivity.class,
                TestVisumAccountAuthenticatorActivity.CONTAINER_ID,
                new TestVisumChildFragment()
        );

        testDetachFragmentOnDetachActivity(
                TestVisumAccountAuthenticatorActivity.class,
                TestVisumAccountAuthenticatorActivity.CONTAINER_ID,
                new TestVisumChildDialogFragment()
        );

    }

    @SuppressWarnings("ResourceType")
    private <A extends FragmentActivity & VisumConfigurableResultReceiver, F extends Fragment & VisumResultReceiver>
    void testDetachFragmentOnDetachActivity(Class<A> activityClass, int containerId, F fragment) {

        ActivityController<A> activityController = Robolectric.buildActivity(activityClass);

        // create an activity and add a fragment
        A testActivity = activityController.setup().get();
        testActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment)
                .commit();

        // emulates a part of new activity start process
        activityController.pause().stop();
        assertPresenterDetached(testPresenter, CHILD_VIEW_ID, fragment);

    }

    @Test
    public void visumDialogFragment() {
        testFragment(new TestVisumDialogFragment());
    }

    @Test
    public void visumBottomSheetDialogFragment() {
        testFragment(new TestVisumBottomSheetDialogFragment());
    }

    @Test
    public void visumFragment() {
        testFragment(new TestVisumFragment());
    }

    @Test
    public void visumWidget() {

        FragmentContainerActivity fragmentContainerActivity = Robolectric.setupActivity(FragmentContainerActivity.class);

        TestVisumWidget testView = new TestVisumWidget(RuntimeEnvironment.application);

        fragmentContainerActivity.view.addView(testView);
        assertPresenterAttached(testPresenter, VIEW_ID, testView);

        fragmentContainerActivity.view.removeView(testView);
        assertPresenterDetached(testPresenter, VIEW_ID, testView);

    }

    @SuppressWarnings({"ResourceType", "unchecked"})
    @Test
    public void visumChildFragment() {
        visumChildFragment(new TestVisumFragment(), new TestVisumChildFragment());
        visumChildFragment(new TestVisumFragment(), new TestVisumChildDialogFragment());
        visumChildFragment(new TestVisumFragment(), new TestVisumChildBottomSheetDialogFragment());
    }

    @SuppressWarnings({"ResourceType", "unchecked"})
    @Test
    public void visumChildDialogFragment() {
        visumChildFragment(new TestVisumDialogFragment(), new TestVisumChildFragment());
        visumChildFragment(new TestVisumDialogFragment(), new TestVisumChildDialogFragment());
        visumChildFragment(new TestVisumDialogFragment(), new TestVisumChildBottomSheetDialogFragment());
    }

    @SuppressWarnings({"ResourceType", "unchecked"})
    @Test
    public void visumChildBottomSheetDialogFragment() {
        visumChildFragment(new TestVisumBottomSheetDialogFragment(), new TestVisumChildFragment());
        visumChildFragment(new TestVisumBottomSheetDialogFragment(), new TestVisumChildDialogFragment());
        visumChildFragment(new TestVisumBottomSheetDialogFragment(), new TestVisumChildBottomSheetDialogFragment());
    }

    @SuppressWarnings({"ResourceType", "unchecked"})
    protected <V extends Fragment & VisumDynamicPresenterView> void visumChildFragment(V parentView, V childView) {

        ActivityController<FragmentContainerActivity> activityController = Robolectric.buildActivity(FragmentContainerActivity.class);
        FragmentContainerActivity fragmentContainerActivity = activityController.setup().get();

        // create a parent fragment
        fragmentContainerActivity.getSupportFragmentManager()
                .beginTransaction()
                .add(FragmentContainerActivity.CONTAINER_ID, parentView)
                .commit();

        // create a child fragment
        parentView.getChildFragmentManager()
                .beginTransaction()
                .add(R.id.test_container, childView)
                .commit();

        // hide the parent fragment
        fragmentContainerActivity.getSupportFragmentManager()
                .beginTransaction()
                .hide(parentView)
                .commit();
        assertPresenterDetached(testPresenter, CHILD_VIEW_ID, childView);

        // show the parent fragment
        fragmentContainerActivity.getSupportFragmentManager()
                .beginTransaction()
                .show(parentView)
                .commit();
        assertPresenterAttached(testPresenter, CHILD_VIEW_ID, childView);

    }

    @SuppressWarnings({"ResourceType", "unchecked"})
    protected <V extends Fragment & VisumResultReceiver> void testFragment(V testView) {

        ActivityController<FragmentContainerActivity> activityController = Robolectric.buildActivity(FragmentContainerActivity.class);
        FragmentContainerActivity fragmentContainerActivity = activityController.setup().get();

        // create
        fragmentContainerActivity.getSupportFragmentManager()
                .beginTransaction()
                .add(FragmentContainerActivity.CONTAINER_ID, testView)
                .commit();
        assertPresenterAttached(testPresenter, VIEW_ID, testView);

        // on activity result
        testView.startActivityForResult();
        activityController.pause();
        Shadows.shadowOf(fragmentContainerActivity).receiveResult(
                new Intent(fragmentContainerActivity, ChildActivity.class),
                Activity.RESULT_OK,
                new Intent()
        );
        activityController.resume();
        assertPresenterAttachedBeforeOnActivityResult(testView);

        // hide
        fragmentContainerActivity.getSupportFragmentManager().beginTransaction().hide(testView).commit();
        assertPresenterDetached(testPresenter, VIEW_ID, testView);

        // show
        fragmentContainerActivity.getSupportFragmentManager().beginTransaction().show(testView).commit();
        assertPresenterAttached(testPresenter, VIEW_ID, testView);

        // config change
        Func1<FragmentContainerActivity, V> viewFinder = new Func1<FragmentContainerActivity, V>() {

            @Override
            public V call(FragmentContainerActivity testActivity) {
                return (V) testActivity
                        .getSupportFragmentManager()
                        .findFragmentById(FragmentContainerActivity.CONTAINER_ID);
            }

        };
        fragmentContainerActivity = simulateConfigChange(activityController, FragmentContainerActivity.class, viewFinder).get();
        testView = viewFinder.call(fragmentContainerActivity);

        // destroy
        fragmentContainerActivity.getSupportFragmentManager().beginTransaction().remove(testView).commit();
        assertPresenterDetached(testPresenter, VIEW_ID, testView);

    }

    @SuppressWarnings({"ResourceType", "unchecked"})
    protected <V extends FragmentActivity & VisumConfigurableResultReceiver> void testActivity(Class<V> activityClass) {

        ActivityController<V> activityController = Robolectric.buildActivity(activityClass);
        V testView;

        // create
        testView = activityController.setup().get();
        assertPresenterAttached(testPresenter, VIEW_ID, testView);

        // on activity result
        testView.startActivityForResult();
        activityController.pause();
        Shadows.shadowOf(testView).receiveResult(
                new Intent(testView, ChildActivity.class),
                Activity.RESULT_OK,
                new Intent()
        );
        activityController.resume();

        assertPresenterAttachedBeforeOnActivityResult(testView);

        // config change
        Func1<V, V> viewFinder = new Func1<V, V>() {

            @Override
            public V call(V v) {
                return v;
            }

        };
        activityController = simulateConfigChange(activityController, activityClass, viewFinder);
        testView = viewFinder.call(activityController.get());

        // destroy
        activityController
                .pause()
                .stop()
                .destroy();
        assertPresenterDetached(testPresenter, VIEW_ID, testView);

    }

    private <C extends FragmentActivity & Configurable, V extends VisumView> ActivityController<C> simulateConfigChange(
            ActivityController<C> activityController,
            Class<C> activityClass,
            Func1<C, V> viewFinder
    ) {

        C t = activityController.get();
        t.setChangingConfigurations(true);
        V oldView = viewFinder.call(t);

        Bundle bundle = new Bundle();

        // Destroy the original activity
        activityController
                .pause()
                .saveInstanceState(bundle)
                .stop()
                .destroy();

        // Bring up a new activity
        activityController = Robolectric.buildActivity(activityClass)
                .setup(bundle);
        V newView = viewFinder.call(activityController.get());

        assertPresenterReattached(oldView, newView);

        return activityController;

    }

    public static class TestVisumFragment extends BaseTestVisumFragment {
        public TestVisumFragment() {
            super(VIEW_ID);
        }
    }

    public static class TestVisumChildFragment extends BaseTestVisumFragment {
        public TestVisumChildFragment() {
            super(CHILD_VIEW_ID);
        }
    }

    public static class TestVisumDialogFragment extends BaseTestVisumDialogFragment {
        public TestVisumDialogFragment() {
            super(VIEW_ID);
        }
    }

    public static class TestVisumChildDialogFragment extends BaseTestVisumDialogFragment {
        public TestVisumChildDialogFragment() {
            super(CHILD_VIEW_ID);
        }
    }

    public static class TestVisumBottomSheetDialogFragment extends BaseTestVisumBottomSheetDialogFragment {
        public TestVisumBottomSheetDialogFragment() {
            super(VIEW_ID);
        }
    }

    public static class TestVisumChildBottomSheetDialogFragment extends BaseTestVisumBottomSheetDialogFragment {
        public TestVisumChildBottomSheetDialogFragment() {
            super(CHILD_VIEW_ID);
        }
    }

    public static class ChildActivity extends FragmentActivity {

        @Override
        protected void onStart() {
            super.onStart();
            setResult(RESULT_OK);
            finish();
        }

    }

    public static class TestComponent {

        private final VisumViewTest visumViewTest;

        public TestComponent(VisumViewTest visumViewTest) {
            this.visumViewTest = visumViewTest;
        }

        public TestSubComponent testSubComponent() {
            return new TestSubComponent(visumViewTest);
        }

    }

    private static class FragmentContainerActivity extends FragmentActivity
            implements Configurable {

        private static final int CONTAINER_ID = 1;

        private LinearLayout view;

        private boolean changingConfigurations;

        @SuppressWarnings("ResourceType")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            view = new LinearLayout(this);
            view.setId(CONTAINER_ID);
            setContentView(view);
        }

        @Override
        public boolean isChangingConfigurations() {
            return changingConfigurations;
        }

        @Override
        public void setChangingConfigurations(boolean changingConfigurations) {
            this.changingConfigurations = changingConfigurations;
        }

    }

    public static class TestSubComponent {

        private TestPresenter testPresenter;

        private final VisumViewTest visumViewTest;

        public TestSubComponent(VisumViewTest visumViewTest) {
            this.visumViewTest = visumViewTest;
        }

        @SuppressWarnings("unused")
        public void inject(VisumDynamicPresenterView testVisumView) {
            if (testPresenter == null) {
                testPresenter = new TestPresenter();
                visumViewTest.testPresenter = testPresenter;
            }
            testVisumView.setPresenter(testPresenter);
        }

    }

}
