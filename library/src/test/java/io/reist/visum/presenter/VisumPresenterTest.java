package io.reist.visum.presenter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumTest;
import io.reist.visum.view.BaseTestView;
import rx.functions.Func0;

import static io.reist.visum.presenter.PresenterAssert.assertPresenterAttached;
import static io.reist.visum.presenter.PresenterAssert.assertPresenterDetached;

/**
 * Created by Reist on 26.05.16.
 */
public class VisumPresenterTest extends VisumTest<BaseTestView> {

    private static final int VIEW_ID = 1;

    private static final int VIEW_ID_TWO = 2;

    private TestPresenter presenter;

    private TestViewTwo viewTwo;

    @Before
    public void start() {

        presenter = new TestPresenter();

        setUp(
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

        tearDown();

        viewTwo = null;

    }

    @Test
    public void testSetView() {

        BaseTestView view = getClient();

        view.attachPresenter();
        assertPresenterAttached(presenter, VIEW_ID, view);

        view.detachPresenter();
        assertPresenterDetached(presenter, VIEW_ID, view);

    }

    @Test
    public void testMultiViews() {

        BaseTestView viewOne = getClient();

        viewOne.attachPresenter();
        assertPresenterAttached(presenter, VIEW_ID, viewOne);

        viewTwo.attachPresenter();
        assertPresenterAttached(presenter, VIEW_ID_TWO, viewTwo);

        Assert.assertEquals("Invalid view count", 2, presenter.getViewCount());

        viewOne.detachPresenter();
        assertPresenterDetached(presenter, VIEW_ID, viewOne);

        viewTwo.detachPresenter();
        assertPresenterDetached(presenter, VIEW_ID_TWO, viewTwo);

    }

    @Override
    protected BaseTestView createClient() {
        viewTwo = new TestViewTwo(getComponentCache(), VIEW_ID_TWO);
        return new TestViewOne(getComponentCache(), VIEW_ID);
    }

    private class TestViewOne extends BaseTestView {
        protected TestViewOne(ComponentCache componentCache, int viewId) {
            super(componentCache, presenter, viewId);
        }
    }

    private class TestViewTwo extends BaseTestView {
        protected TestViewTwo(ComponentCache componentCache, int viewId) {
            super(componentCache, presenter, viewId);
        }
    }

    private static class TestComponent {}

}
