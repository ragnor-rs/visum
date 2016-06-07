package io.reist.visum.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
    protected <V extends Fragment & TestVisumResultReceiver> void testFragment(V testView) {

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
                new Intent(fragmentContainerActivity, SubActivity.class),
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
    protected <V extends FragmentActivity & TestVisumActivityView> void testActivity(Class<V> activityClass) {

        ActivityController<V> activityController = Robolectric.buildActivity(activityClass);
        V testView;

        // create
        testView = activityController.setup().get();
        assertPresenterAttached(testPresenter, VIEW_ID, testView);

        // on activity result
        testView.startActivityForResult();
        Shadows.shadowOf(testView).receiveResult(
                new Intent(testView, SubActivity.class),
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

        assertPresenterReattached(oldView, newView);

        return activityController;

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

    public static class TestVisumFragment extends VisumFragment<TestPresenter>
            implements TestVisumResultReceiver {

        private static final int REQUEST_CODE = 1;

        private final TestVisumResultReceiver dummy = Mockito.mock(TestVisumResultReceiver.class);

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

        @Override
        public void startActivityForResult() {
            startActivityForResult(new Intent(getActivity(), SubActivity.class), REQUEST_CODE);
        }

        @Override
        public TestVisumResultReceiver getDummy() {
            return dummy;
        }

        @Override
        public void onActivityResult() {}

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            dummy.onActivityResult();
        }

        @Override
        public void attachPresenter() {
            super.attachPresenter();
            dummy.attachPresenter();
        }

    }

    public static class TestVisumDialogFragment extends VisumDialogFragment<TestPresenter>
            implements TestVisumResultReceiver {

        private static final int REQUEST_CODE = 1;

        private final TestVisumResultReceiver dummy = Mockito.mock(TestVisumResultReceiver.class);

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

        @Override
        public void startActivityForResult() {
            startActivityForResult(new Intent(getActivity(), SubActivity.class), REQUEST_CODE);
        }

        @Override
        public TestVisumResultReceiver getDummy() {
            return dummy;
        }

        @Override
        public void onActivityResult() {}

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            dummy.onActivityResult();
        }

        @Override
        public void attachPresenter() {
            super.attachPresenter();
            dummy.attachPresenter();
        }

    }

    public static class TestVisumActivity extends VisumActivity<TestPresenter>
            implements TestVisumActivityView {

        private static final int REQUEST_CODE = 1;

        private TestPresenter presenter;

        private boolean changingConfigurations;

        private final TestVisumResultReceiver dummy = Mockito.mock(TestVisumResultReceiver.class);

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

        @Override
        public void startActivityForResult() {
            startActivityForResult(new Intent(this, SubActivity.class), REQUEST_CODE);
        }

        @Override
        public TestVisumResultReceiver getDummy() {
            return dummy;
        }

        @Override
        public void onActivityResult() {}

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            dummy.onActivityResult();
        }

        @Override
        public void attachPresenter() {
            super.attachPresenter();
            dummy.attachPresenter();
        }

    }

    public static class SubActivity extends FragmentActivity {

        @Override
        protected void onStart() {
            super.onStart();
            setResult(RESULT_OK);
            finish();
        }

    }

    interface TestActivity {

        void setChangingConfigurations(boolean changingConfigurations);

    }

    interface TestVisumActivityView extends TestActivity, TestVisumResultReceiver {}

    public static class TestVisumAccountAuthenticatorActivity
            extends VisumAccountAuthenticatorActivity<TestPresenter>
            implements TestVisumActivityView {

        private static final int REQUEST_CODE = 1;

        private final TestVisumResultReceiver dummy = Mockito.mock(TestVisumResultReceiver.class);

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

        @Override
        public void startActivityForResult() {
            startActivityForResult(new Intent(this, SubActivity.class), REQUEST_CODE);
        }

        @Override
        public TestVisumResultReceiver getDummy() {
            return dummy;
        }

        @Override
        public void onActivityResult() {}

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            dummy.onActivityResult();
        }

        @Override
        public void attachPresenter() {
            super.attachPresenter();
            dummy.attachPresenter();
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
