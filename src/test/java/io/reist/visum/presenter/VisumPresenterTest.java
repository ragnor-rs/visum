package io.reist.visum.presenter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import io.reist.visum.BuildConfig;
import io.reist.visum.TestApplication;
import io.reist.visum.VisumTest;
import io.reist.visum.view.VisumBaseView;

/**
 * Created by Reist on 26.05.16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = {Build.VERSION_CODES.JELLY_BEAN},
        application = TestApplication.class
)
public class VisumPresenterTest extends VisumTest<VisumPresenterTest.TestComponent> {

    private static final int VIEW_ID = 1;

    private TestPresenter testPresenter;

    public VisumPresenterTest() {
        super(VisumPresenterTest.TestComponent.class);
    }

    @Before
    public void setUp() throws Exception {
        register(TestView.class);
        testPresenter = new TestPresenter();
    }

    @After
    public void tearDown() throws Exception {
        testPresenter = null;
    }

    @Test
    public void visumPresenter() {

        TestView testView = new TestView(VIEW_ID, RuntimeEnvironment.application);

        testView.attachPresenter();
        checkPresenterAttached(testView);

        testView.detachPresenter();
        checkPresenterDetached(testView);

    }

    private void checkPresenterDetached(TestView testView) {
        InOrder inOrder = Mockito.inOrder(testPresenter.dummy);
        inOrder.verify(testPresenter.dummy, Mockito.times(1)).onViewDetached(VIEW_ID, testView);
        inOrder.verify(testPresenter.dummy, Mockito.times(1)).onStop();
    }

    private void checkPresenterAttached(TestView testView) {
        InOrder inOrder = Mockito.inOrder(testPresenter.dummy, testComponent);
        inOrder.verify(testComponent, Mockito.times(1)).inject(testView);
        inOrder.verify(testPresenter.dummy, Mockito.times(1)).onStart();
        inOrder.verify(testPresenter.dummy, Mockito.times(1)).onViewAttached(VIEW_ID, testView);
    }

    private static class TestPresenter extends VisumPresenter<TestView> {

        private final TestPresenter dummy = Mockito.mock(TestPresenter.class);

        @Override
        protected void onStop() {
            dummy.onStop();
        }

        @Override
        protected void onStart() {
            dummy.onStart();
        }

        @Override
        protected void onViewAttached(int id, @NonNull TestView view) {
            dummy.onViewAttached(id, view);
        }

        @Override
        protected void onViewDetached(int id, @NonNull TestView view) {
            dummy.onViewDetached(id, view);
        }

    }

    private class TestView extends VisumBaseView<TestPresenter> {

        public TestView(int viewId, Context context) {
            super(viewId, context);
        }

        @Override
        public TestPresenter getPresenter() {
            return testPresenter;
        }

        @Override
        public void inject(@NonNull Object from) {
            ((TestComponent) from).inject(this);
        }

    }

    protected interface TestComponent {
        void inject(TestView testView);
    }

}
