package io.reist.visum.presenter;

import android.support.annotation.NonNull;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Arrays;

import io.reist.visum.ComponentCache;
import io.reist.visum.ComponentCacheTest;
import io.reist.visum.VisumTest;
import io.reist.visum.view.VisumView;
import rx.functions.Func0;

/**
 * Created by Reist on 26.05.16.
 */
public class VisumPresenterTest extends VisumTest<VisumPresenterTest.TestView> {

    private static final int VIEW_ID = 1;

    private static final int VIEW_ID_TWO = 2;

    private TestPresenter presenter;

    private TestViewTwo viewTwo;

    @Before
    public void start() {

        presenter = new TestPresenter();

        start(
                Arrays.asList(TestViewOne.class, TestViewTwo.class),
                new Func0<Object>() {

                    @Override
                    public Object call() {
                        return new TestComponent();
                    }

                }
        );

        viewTwo = new TestViewTwo(getComponentCache(), VIEW_ID_TWO);

    }

    @After
    public void finish() {

        presenter = null;

        stop();

        viewTwo = null;

    }

    @Test
    public void testSetView() {

        TestView view = getClient();

        view.attachPresenter();
        presenter.checkPresenterAttached(VIEW_ID, view);

        view.detachPresenter();
        presenter.checkPresenterDetached(VIEW_ID, view);

    }

    @Test
    public void testFewViews() {

        TestView viewOne = getClient();
        TestView viewTwo = new TestViewTwo(getComponentCache(), VIEW_ID_TWO);

        viewOne.attachPresenter();
        presenter.checkPresenterAttached(VIEW_ID, viewOne);

        viewTwo.attachPresenter();
        presenter.checkPresenterAttached(VIEW_ID_TWO, viewTwo);

        Mockito.verify(presenter.dummy, Mockito.times(1)).onStart();

        Assert.assertEquals("Invalid view count", 2, presenter.getViewCount());

        viewOne.detachPresenter();
        presenter.checkPresenterDetached(VIEW_ID, viewOne);

        viewTwo.detachPresenter();
        presenter.checkPresenterDetached(VIEW_ID_TWO, viewTwo);

        Mockito.verify(presenter.dummy, Mockito.times(1)).onStop();

    }

    @Override
    protected TestView createClient() {
        viewTwo = new TestViewTwo(getComponentCache(), VIEW_ID_TWO);
        return new TestViewOne(getComponentCache(), VIEW_ID);
    }

    protected static abstract class TestView
            extends ComponentCacheTest.TestClient
            implements VisumView<TestPresenter> {

        private final TestPresenter presenter;
        private final int viewId;

        protected TestView(ComponentCache componentCache, TestPresenter presenter, int viewId) {
            super(componentCache);
            this.presenter = presenter;
            this.viewId = viewId;
        }

        @Override
        public TestPresenter getPresenter() {
            return presenter;
        }

        @Override
        public void attachPresenter() {
            onStartClient();
            presenter.setView(viewId, this);
        }

        @Override
        public void detachPresenter() {
            presenter.setView(viewId, null);
            onStopClient();
        }

    }

    private class TestViewOne extends TestView {
        protected TestViewOne(ComponentCache componentCache, int viewId) {
            super(componentCache, presenter, viewId);
        }
    }

    private class TestViewTwo extends TestView {
        protected TestViewTwo(ComponentCache componentCache, int viewId) {
            super(componentCache, presenter, viewId);
        }
    }

    private static class TestComponent {}

    protected static class TestPresenter extends VisumPresenter<VisumView> {

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
        protected void onViewAttached(int id, @NonNull VisumView view) {
            dummy.onViewAttached(id, view);
        }

        @Override
        protected void onViewDetached(int id, @NonNull VisumView view) {
            dummy.onViewDetached(id, view);
        }

        protected void checkPresenterDetached(int viewId, VisumView<TestPresenter> view) {
            InOrder inOrder = Mockito.inOrder(dummy);
            inOrder.verify(dummy, Mockito.times(1)).onViewDetached(viewId, view);
            if (getViewCount() == 0) {
                inOrder.verify(dummy, Mockito.times(1)).onStop();
            }
        }

        protected void checkPresenterAttached(int viewId, VisumView<TestPresenter> view) {
            InOrder inOrder = Mockito.inOrder(dummy);
            if (getViewCount() == 1) {
                inOrder.verify(dummy, Mockito.times(1)).onStart();
            }
            inOrder.verify(dummy, Mockito.times(1)).onViewAttached(viewId, view);
        }

    }

}
