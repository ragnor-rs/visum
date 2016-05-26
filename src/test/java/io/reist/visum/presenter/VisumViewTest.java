package io.reist.visum.presenter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import io.reist.visum.BuildConfig;
import io.reist.visum.TestApplication;
import io.reist.visum.VisumImplTest;
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
public class VisumViewTest extends VisumImplTest<VisumViewTest.TestComponent> {

    private static final int VIEW_ID = 1;

    private VisumPresenterTest.TestPresenter testPresenter;

    private TestVisumBaseView testVisumBaseView;

    public VisumViewTest() {
        super(VisumViewTest.TestComponent.class);
    }

    @Before
    public void setUp() throws Exception {

        register(TestVisumBaseView.class);

        testVisumBaseView = new TestVisumBaseView(VIEW_ID, RuntimeEnvironment.application);

        testPresenter = new VisumPresenterTest.TestPresenter();

    }

    @After
    public void tearDown() throws Exception {

        testVisumBaseView = null;

        testPresenter = null;

    }

    @Test
    public void visumBaseView() {

        testVisumBaseView.attachPresenter();
        testPresenter.checkPresenterAttached(VIEW_ID, testVisumBaseView);

        testVisumBaseView.detachPresenter();
        testPresenter.checkPresenterDetached(VIEW_ID, testVisumBaseView);

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

    private class TestVisumBaseView extends VisumBaseView<VisumPresenterTest.TestPresenter> {

        public TestVisumBaseView(int viewId, Context context) {
            super(viewId, context);
        }

        @Override
        public VisumPresenterTest.TestPresenter getPresenter() {
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
