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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.Arrays;

import io.reist.visum.BuildConfig;
import io.reist.visum.TestApplication;
import io.reist.visum.VisumImplTest;
import io.reist.visum.presenter.TestPresenter;

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

    private TestPresenter testPresenter;

    private static final int VIEW_ID = 1;

    public VisumViewTest() {
        super(VisumViewTest.TestComponent.class);
        register(Arrays.asList(
                TestVisumBaseView.class,
                TestVisumFragment.class,
                TestVisumDialogFragment.class,
                TestVisumActivity.class,
                TestVisumAccountAuthenticatorActivity.class,
                TestVisumWidget.class
        ));
    }

    @After
    public void tearDown() {
        testPresenter = null;
    }

    @Test
    public void visumBaseView() {

        TestVisumBaseView testView = new TestVisumBaseView(RuntimeEnvironment.application);

        testView.attachPresenter();
        testPresenter.checkPresenterAttached(VIEW_ID, testView);

        testView.detachPresenter();
        testPresenter.checkPresenterDetached(VIEW_ID, testView);

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

        TestActivity testActivity = Robolectric.setupActivity(TestActivity.class);

        TestVisumWidget testView = new TestVisumWidget(RuntimeEnvironment.application);

        testActivity.view.addView(testView);
        testPresenter.checkPresenterAttached(VIEW_ID, testView);

        testActivity.view.removeView(testView);
        testPresenter.checkPresenterDetached(VIEW_ID, testView);

    }

    @SuppressWarnings({"ResourceType", "unchecked"})
    protected <V extends Fragment & VisumView<TestPresenter>> void testFragment(V testView) {

        ActivityController<TestActivity> activityController = Robolectric.buildActivity(TestActivity.class);
        TestActivity testActivity = activityController.setup().get();

        // create
        testActivity.getSupportFragmentManager()
                .beginTransaction()
                .add(TestActivity.CONTAINER_ID, testView)
                .commit();
        testPresenter.checkPresenterAttached(VIEW_ID, testView);

        // hide
        testActivity.getSupportFragmentManager().beginTransaction().hide(testView).commit();
        testPresenter.checkPresenterDetached(VIEW_ID, testView);

        // show
        testActivity.getSupportFragmentManager().beginTransaction().show(testView).commit();
        testPresenter.checkPresenterAttached(VIEW_ID, testView);

        // config change
        testActivity = simulateConfigChange(activityController, TestActivity.class).get();
        V newTestView = (V) testActivity
                .getSupportFragmentManager()
                .findFragmentById(TestActivity.CONTAINER_ID);
        checkViewRecreated(testView, newTestView);
        testView = newTestView;

        // destroy
        testActivity.getSupportFragmentManager().beginTransaction().remove(testView).commit();
        testPresenter.checkPresenterDetached(VIEW_ID, testView);

    }

    @SuppressWarnings({"ResourceType", "unchecked"})
    protected <V extends FragmentActivity & VisumView<TestPresenter>> void testActivity(Class<V> activityClass) {

        ActivityController<V> activityController = Robolectric.buildActivity(activityClass);
        V testView;

        // create
        testView = activityController.setup().get();
        testPresenter.checkPresenterAttached(VIEW_ID, testView);

        // config change
        activityController = simulateConfigChange(activityController, activityClass);
        V newTestView = activityController.get();
        checkViewRecreated(testView, newTestView);
        testView = newTestView;

        // destroy
        activityController
                .pause()
                .stop()
                .destroy();
        testPresenter.checkPresenterDetached(VIEW_ID, testView);

    }

    private <C extends FragmentActivity> ActivityController<C> simulateConfigChange(ActivityController<C> activityController, Class<C> activityClass) {

        Bundle bundle = new Bundle();

        // Destroy the original activity
        activityController
                .saveInstanceState(bundle)
                .pause()
                .stop()
                .destroy();

        // Bring up a new activity
        activityController = Robolectric.buildActivity(activityClass)
                .create(bundle)
                .start()
                .restoreInstanceState(bundle)
                .resume()
                .visible();

        return activityController;

    }

    private <V extends VisumView<TestPresenter>> void checkViewRecreated(V oldTestView, V newTestView) {
        Assert.assertFalse("View has not been recreated", newTestView == oldTestView);
        testPresenter.checkPresenterDetached(VIEW_ID, oldTestView);
        testPresenter.checkPresenterAttached(VIEW_ID, newTestView);
    }

    @Override
    protected TestComponent mock() {

        TestComponent component = super.mock();

        Mockito.doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                TestVisumView view = (TestVisumView) arguments[0];
                if (testPresenter == null) {
                    testPresenter = new TestPresenter();
                }
                view.setPresenter(testPresenter);
                return null;
            }

        }).when(component).inject(Mockito.any(TestVisumView.class));

        return component;

    }

    private interface TestVisumView {
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
            ((TestComponent) from).inject(this);
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
            ((TestComponent) from).inject(this);
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
            ((TestComponent) from).inject(this);
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

    public static class TestVisumActivity extends VisumActivity<TestPresenter> implements TestVisumView {

        private TestPresenter presenter;

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
            ((TestComponent) from).inject(this);
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
        }

    }

    public static class TestVisumAccountAuthenticatorActivity
            extends VisumAccountAuthenticatorActivity<TestPresenter>
            implements TestVisumView {

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
            ((TestComponent) from).inject(this);
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
            ((TestComponent) from).inject(this);
        }

        @Override
        public void setPresenter(TestPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        protected void inflate() {}

    }

    protected interface TestComponent {
        void inject(TestVisumView testVisumView);
    }

    private static class TestActivity extends FragmentActivity {

        private static final int CONTAINER_ID = 1;

        private LinearLayout view;

        @SuppressWarnings("ResourceType")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            view = new LinearLayout(this);
            view.setId(CONTAINER_ID);
            setContentView(view);
        }

    }

}
