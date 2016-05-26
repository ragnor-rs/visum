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
public class VisumViewTest extends VisumTest<VisumViewTest.TestComponent> {

    private static final int VIEW_ID = 1;

    private TestPresenter testPresenter;

    private TestVisumBaseView testVisumBaseView;

    public VisumViewTest() {
        super(VisumViewTest.TestComponent.class);
    }

    @Before
    public void setUp() throws Exception {

        register(TestVisumBaseView.class);
        testPresenter = new TestPresenter();

        testVisumBaseView = new TestVisumBaseView(VIEW_ID, RuntimeEnvironment.application);

    }

    @After
    public void tearDown() throws Exception {

        testVisumBaseView = null;

        testPresenter = null;

    }

    @Test
    public void visumBaseView() {

        testVisumBaseView.attachPresenter();
        checkPresenterAttached(testVisumBaseView);

        testVisumBaseView.detachPresenter();
        checkPresenterDetached(testVisumBaseView);

    }

    @Test
    public void visumAccountAuthenticatorActivity() {
        // TODO
    }

    @Test
    public void visumActivity() {
        // TODO
    }

    @Test
    public void visumDialogFragment() {
        // TODO
    }

    @Test
    public void visumFragment() {
        // TODO
    }

    @Test
    public void visumWidget() {
        // TODO
    }

    private void checkPresenterDetached(TestVisumBaseView testVisumBaseView) {
        InOrder inOrder = Mockito.inOrder(testPresenter.dummy);
        inOrder.verify(testPresenter.dummy, Mockito.times(1)).onViewDetached(VIEW_ID, testVisumBaseView);
        inOrder.verify(testPresenter.dummy, Mockito.times(1)).onStop();
    }

    private void checkPresenterAttached(TestVisumBaseView testVisumBaseView) {
        InOrder inOrder = Mockito.inOrder(testPresenter.dummy, testComponent);
        inOrder.verify(testComponent, Mockito.times(1)).inject(testVisumBaseView);
        inOrder.verify(testPresenter.dummy, Mockito.times(1)).onStart();
        inOrder.verify(testPresenter.dummy, Mockito.times(1)).onViewAttached(VIEW_ID, testVisumBaseView);
    }

    private static class TestPresenter extends VisumPresenter<TestVisumBaseView> {

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
        protected void onViewAttached(int id, @NonNull TestVisumBaseView view) {
            dummy.onViewAttached(id, view);
        }

        @Override
        protected void onViewDetached(int id, @NonNull TestVisumBaseView view) {
            dummy.onViewDetached(id, view);
        }

    }

    private class TestVisumBaseView extends VisumBaseView<TestPresenter> {

        public TestVisumBaseView(int viewId, Context context) {
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
        void inject(TestVisumBaseView testVisumBaseView);
    }

}
