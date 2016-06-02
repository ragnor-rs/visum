package io.reist.visum.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import io.reist.visum.BuildConfig;
import io.reist.visum.TestApplication;
import io.reist.visum.VisumImplTest;
import io.reist.visum.presenter.TestPresenter;
import io.reist.visum.presenter.VisumPresenter;
import rx.functions.Func0;
import rx.functions.Func1;

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

    private static final int VIEW_ID = 1;

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
                    public void inject(TestVisumView testVisumView) {
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
                TestVisumBaseView.class,
                TestVisumFragment.class,
                TestVisumDialogFragment.class,
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

        TestVisumBaseView testView = new TestVisumBaseView(RuntimeEnvironment.application);

        testView.attachPresenter();
        testPresenter.assertPresenterAttached(VIEW_ID, testView);

        testView.detachPresenter();
        testPresenter.assertPresenterDetached(VIEW_ID, testView);

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
        testPresenter.assertPresenterAttached(VIEW_ID, testView);

        fragmentContainerActivity.view.removeView(testView);
        testPresenter.assertPresenterDetached(VIEW_ID, testView);

    }

    @SuppressWarnings({"ResourceType", "unchecked"})
    protected <V extends Fragment & TestVisumView> void testFragment(V testView) {

        ActivityController<FragmentContainerActivity> activityController = Robolectric.buildActivity(FragmentContainerActivity.class);
        FragmentContainerActivity fragmentContainerActivity = activityController.setup().get();

        // create
        fragmentContainerActivity.getSupportFragmentManager()
                .beginTransaction()
                .add(FragmentContainerActivity.CONTAINER_ID, testView)
                .commit();
        testPresenter.assertPresenterAttached(VIEW_ID, testView);

        // hide
        fragmentContainerActivity.getSupportFragmentManager().beginTransaction().hide(testView).commit();
        testPresenter.assertPresenterDetached(VIEW_ID, testView);

        // show
        fragmentContainerActivity.getSupportFragmentManager().beginTransaction().show(testView).commit();
        testPresenter.assertPresenterAttached(VIEW_ID, testView);

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
        testPresenter.assertPresenterDetached(VIEW_ID, testView);

    }

    @SuppressWarnings({"ResourceType", "unchecked"})
    protected <V extends FragmentActivity & TestVisumActivityView> void testActivity(Class<V> activityClass) {

        ActivityController<V> activityController = Robolectric.buildActivity(activityClass);
        V testView;

        // create
        testView = activityController.setup().get();
        testPresenter.assertPresenterAttached(VIEW_ID, testView);

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
        testPresenter.assertPresenterDetached(VIEW_ID, testView);

    }

    private <C extends FragmentActivity & TestActivity, V extends VisumView> ActivityController<C> simulateConfigChange(
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

        assertConfigChange(oldView, newView);

        return activityController;

    }

    private void assertConfigChange(VisumView oldTestView, VisumView newTestView) {

        Assert.assertFalse("View has not been recreated", newTestView == oldTestView);

        VisumPresenter oldPresenter = oldTestView.getPresenter();
        VisumPresenter newPresenter = newTestView.getPresenter();
        Assert.assertNotNull("No presenter is attached to the new instance of the view", newPresenter);
        Assert.assertTrue("Presenter didn't survive", oldPresenter == newPresenter);

    }

    private interface TestVisumView extends VisumView<TestPresenter> {

        void setPresenter(TestPresenter testPresenter);

    }

    private static class TestVisumBaseView extends VisumBaseView<TestPresenter> implements TestVisumView {

        private TestPresenter presenter;

        public TestVisumBaseView(Context context) {
            super(VIEW_ID, context);
        }

        @Override
        public TestPresenter getPresenter() {
            return presenter;
        }

        @Override
        public void inject(@NonNull Object from) {
            ((TestSubComponent) from).inject(this);
        }

        @Override
        public void setPresenter(TestPresenter presenter) {
            this.presenter = presenter;
        }

    }

    public static class TestVisumFragment extends VisumFragment<TestPresenter> implements TestVisumView {

        private TestPresenter presenter;

        public TestVisumFragment() {
            super(VIEW_ID);
        }

        @Override
        protected int getLayoutRes() {
            return 0;
        }

        @Override
        public TestPresenter getPresenter() {
            return presenter;
        }

        @Override
        public void inject(@NonNull Object from) {
            ((TestSubComponent) from).inject(this);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return new View(getContext());
        }

        @Override
        public void setPresenter(TestPresenter presenter) {
            this.presenter = presenter;
        }

    }

    public static class TestVisumDialogFragment extends VisumDialogFragment<TestPresenter> implements TestVisumView {

        private TestPresenter presenter;

        public TestVisumDialogFragment() {
            super(VIEW_ID);
        }

        @Override
        protected int getLayoutRes() {
            return 0;
        }

        @Override
        public TestPresenter getPresenter() {
            return presenter;
        }

        @Override
        public void inject(@NonNull Object from) {
            ((TestSubComponent) from).inject(this);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return new View(getContext());
        }

        @Override
        public void setPresenter(TestPresenter presenter) {
            this.presenter = presenter;
        }

    }

    public static class TestVisumActivity extends VisumActivity<TestPresenter>
            implements TestVisumActivityView {

        private TestPresenter presenter;

        private boolean changingConfigurations;

        public TestVisumActivity() {
            super(VIEW_ID);
        }

        @Override
        protected int getLayoutRes() {
            return 0;
        }

        @Override
        public TestPresenter getPresenter() {
            return presenter;
        }

        @Override
        public void inject(@NonNull Object from) {
            ((TestSubComponent) from).inject(this);
        }

        @Override
        public void setPresenter(TestPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public void setContentView(@LayoutRes int layoutResID) {
            setContentView(new View(this));
        }

        @SuppressLint("PrivateResource")
        @Override
        public void onCreate(Bundle savedInstanceState) {
            setTheme(android.support.v7.appcompat.R.style.Theme_AppCompat);
            super.onCreate(savedInstanceState);
            changingConfigurations = false;
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

    interface TestActivity {

        void setChangingConfigurations(boolean changingConfigurations);

    }

    interface TestVisumActivityView extends TestVisumView, TestActivity {}

    public static class TestVisumAccountAuthenticatorActivity
            extends VisumAccountAuthenticatorActivity<TestPresenter>
            implements TestVisumActivityView {

        private boolean changingConfigurations;

        private TestPresenter presenter;

        public TestVisumAccountAuthenticatorActivity() {
            super(VIEW_ID);
        }

        @Override
        protected int getLayoutRes() {
            return 0;
        }

        @Override
        public TestPresenter getPresenter() {
            return presenter;
        }

        @Override
        public void inject(@NonNull Object from) {
            ((TestSubComponent) from).inject(this);
        }

        @Override
        public void setPresenter(TestPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public void setContentView(@LayoutRes int layoutResID) {
            setContentView(new View(this));
        }

        @SuppressLint("PrivateResource")
        @Override
        public void onCreate(Bundle savedInstanceState) {
            setTheme(android.support.v7.appcompat.R.style.Theme_AppCompat);
            super.onCreate(savedInstanceState);
            changingConfigurations = false;
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

    public static class TestVisumWidget extends VisumWidget<TestPresenter> implements TestVisumView {

        private TestPresenter presenter;

        public TestVisumWidget(Context context) {
            super(VIEW_ID, context);
        }

        @Override
        protected int getLayoutRes() {
            return 0;
        }

        @Override
        public TestPresenter getPresenter() {
            return presenter;
        }

        @Override
        public void inject(@NonNull Object from) {
            ((TestSubComponent) from).inject(this);
        }

        @Override
        public void setPresenter(TestPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        protected void inflate() {}

    }

    protected interface TestComponent {
        TestSubComponent testSubComponent();
    }

    protected interface TestSubComponent {
        void inject(TestVisumView testVisumView);
    }

    private static class FragmentContainerActivity extends FragmentActivity
            implements TestActivity {

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
