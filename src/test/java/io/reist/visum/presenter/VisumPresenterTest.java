package io.reist.visum.presenter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumTest;
import io.reist.visum.view.TestView;
import rx.functions.Func0;

/**
 * Created by Reist on 26.05.16.
 */
public class VisumPresenterTest extends VisumTest<TestView> {

    private static final int VIEW_ID = 1;

    private static final int VIEW_ID_TWO = 2;

    private TestPresenter presenter;

    private TestViewTwo viewTwo;

    @Before
    public void start() {

        presenter = new TestPresenter();

        start(
                new Func0<Object>() {

                    @Override
                    public Object call() {
                        return new TestComponent();
                    }

                },
                TestViewOne.class, TestViewTwo.class
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

        viewOne.attachPresenter();
        presenter.checkPresenterAttached(VIEW_ID, viewOne);

        viewTwo.attachPresenter();
        presenter.checkPresenterAttached(VIEW_ID_TWO, viewTwo);

        Assert.assertEquals("Invalid view count", 2, presenter.getViewCount());

        viewOne.detachPresenter();
        presenter.checkPresenterDetached(VIEW_ID, viewOne);

        viewTwo.detachPresenter();
        presenter.checkPresenterDetached(VIEW_ID_TWO, viewTwo);

    }

    @Override
    protected TestView createClient() {
        viewTwo = new TestViewTwo(getComponentCache(), VIEW_ID_TWO);
        return new TestViewOne(getComponentCache(), VIEW_ID);
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

}
