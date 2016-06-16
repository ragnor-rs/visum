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
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import io.reist.visum.BuildConfig;
import io.reist.visum.TestApplication;
import io.reist.visum.VisumImplTest;
import io.reist.visum.presenter.TestPresenter;
import rx.functions.Func0;
import rx.functions.Func1;

import static io.reist.visum.presenter.PresenterAssert.assertPresenterAttached;
import static io.reist.visum.presenter.PresenterAssert.assertPresenterDetached;
import static io.reist.visum.view.ViewAssert.assertPresenterAttachedBeforeOnActivityResult;
import static io.reist.visum.view.ViewAssert.assertPresenterReattached;

/**
 * Created by Reist on 26.05.16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = {Build.VERSION_CODES.JELLY_BEAN},
        application = TestApplication.class
)
public class VisumViewTest extends VisumImplTest<VisumViewTest.TestComponent> {

    static final int VIEW_ID = 1;
    private static final int CHILD_VIEW_ID = 2;

    /**
     * A presenter from a sub-component. It will be null after the sub-component is removed from the
     * {@link io.reist.visum.ComponentCache}.
     */
    private TestPresenter testPresenter;

    @Override
    protected TestComponent createComponent() {
        return new TestComponent() {

            @Override
            public TestSubComponent testSubComponent() {
                testPresenter = null;
                return new TestSubComponent() {

                    TestPresenter testPresenter;

                    @Override
                    public void inject(VisumViewWithPresenterSetter testVisumView) {
                        if (testPresenter == null) {
                            testPresenter = new TestPresenter();
                            VisumViewTest.this.testPresenter = testPresenter;
                        }
                        testVisumView.setPresenter(testPresenter);
                    }

                };
            }

        };
    }

    @Before
    public void start() throws Exception {
        setUp();
        getComponentCache().register(
                new Func0<Object>() {

                    @Override
                    public Object call() {
                        return getComponent().testSubComponent();
                    }

                },
                TestVisumView.class,
                BaseTestVisumFragment.class,
                BaseTestVisumDialogFragment.class,
                TestVisumActivity.class,
                TestVisumAccountAuthenticatorActivity.class,
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
    public void visumDialogFragment() {
        testFragment(new TestVisumDialogFragment());
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
    }

    @SuppressWarnings({"ResourceType", "unchecked"})
    @Test
    public void visumChildDialogFragment() {
        visumChildFragment(new TestVisumDialogFragment(), new TestVisumChildFragment());
        visumChildFragment(new TestVisumDialogFragment(), new TestVisumChildDialogFragment());
    }

    @SuppressWarnings({"ResourceType", "unchecked"})
    protected <V extends Fragment & VisumViewWithPresenterSetter> void visumChildFragment(V parentView, V childView) {

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
                .add(TestVisumFragment.CONTAINER_ID, childView)
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
        Shadows.shadowOf(fragmentContainerActivity).receiveResult(
                new Intent(fragmentContainerActivity, ChildActivity.class),
                Activity.RESULT_OK,
                new Intent()
        );
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
        Shadows.shadowOf(testView).receiveResult(
                new Intent(testView, ChildActivity.class),
                Activity.RESULT_OK,
                new Intent()
        );
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
                .saveInstanceState(bundle)
                .pause()
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

    public static class ChildActivity extends FragmentActivity {

        @Override
        protected void onStart() {
            super.onStart();
            setResult(RESULT_OK);
            finish();
        }

    }

    protected interface TestComponent {
        TestSubComponent testSubComponent();
    }

    protected interface TestSubComponent {
        void inject(VisumViewWithPresenterSetter testVisumView);
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

}
