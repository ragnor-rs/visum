package io.reist.visum.presenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumTest;
import io.reist.visum.view.BaseTestView;
import rx.functions.Func0;

import static io.reist.visum.presenter.PresenterAssert.assertPresenterAttached;
import static io.reist.visum.presenter.PresenterAssert.assertPresenterDetached;

/**
 * Created by 4xes on 17.12.16.
 */
public class SingleViewPresenterTest extends VisumTest<BaseTestView> {

    private TestSingleViewPresenter presenter;

    @Before
    public void start() {

        presenter = new TestSingleViewPresenter();

        setUp(
                new Func0<Object>() {

                    @Override
                    public Object call() {
                        return new TestComponent();
                    }

                },
                TestView.class
        );

    }

    @After
    public void finish() {

        presenter = null;

        tearDown();

    }

    @Test
    public void testSetView() {

        BaseTestView view = getClient();

        view.attachPresenter();
        assertPresenterAttached(presenter, SingleViewPresenter.DEFAULT_VIEW_ID, view);

        view.detachPresenter();
        assertPresenterDetached(presenter, SingleViewPresenter.DEFAULT_VIEW_ID, view);

    }

    @Override
    protected BaseTestView createClient() {
        return new TestView(getComponentCache());
    }

    private class TestView extends BaseTestView {
        protected TestView(ComponentCache componentCache) {
            super(componentCache, presenter);
        }
    }

    private static class TestComponent {}

}
