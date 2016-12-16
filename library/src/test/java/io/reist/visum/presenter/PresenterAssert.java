package io.reist.visum.presenter;

import org.junit.Assert;
import org.mockito.InOrder;
import org.mockito.Mockito;

import io.reist.visum.view.VisumView;
import rx.Single;
import rx.functions.Action1;

/**
 * Created by Reist on 07.06.16.
 */
public class PresenterAssert {

    public static void assertPresenterDetached(TestPresenter presenter, int viewId, VisumView view) {
        TestPresenter dummy = presenter.getDummy();
        InOrder inOrder = Mockito.inOrder(dummy);
        inOrder.verify(dummy, Mockito.times(1)).onViewDetached(viewId, view);
        if (presenter.getViewCount() == 0) {
            inOrder.verify(dummy, Mockito.times(1)).onStop();
        }
    }

    public static void assertPresenterAttached(TestPresenter presenter, int viewId, VisumView view) {
        TestPresenter dummy = presenter.getDummy();
        InOrder inOrder = Mockito.inOrder(dummy);
        if (presenter.getViewCount() == 1) {
            inOrder.verify(dummy, Mockito.times(1)).onStart();
        }
        inOrder.verify(dummy, Mockito.times(1)).onViewAttached(viewId, view);
    }

    public static void assertPresenterDetached(TestSingleViewPresenter presenter, VisumView view) {
        TestSingleViewPresenter dummy = presenter.getDummy();
        InOrder inOrder = Mockito.inOrder(dummy);
        inOrder.verify(dummy, Mockito.times(1)).onViewDetached(view);
    }

    public static void assertPresenterAttached(TestSingleViewPresenter presenter, VisumView view) {
        TestSingleViewPresenter dummy = presenter.getDummy();
        InOrder inOrder = Mockito.inOrder(dummy);
        inOrder.verify(dummy, Mockito.times(1)).onViewAttached(view);
    }

    public static void assertViewSubscribe(int viewId, TestPresenter presenter, boolean expected) {
        try {
            presenter.subscribe(viewId, Single.just(true), new Action1<Boolean>() {

                @Override
                public void call(Boolean aBoolean) {}

            });
            Assert.assertTrue("subscribe(view) shouldn't work here", expected);
        } catch (Exception e) {
            Assert.assertFalse("subscribe(view) should work here", expected);
        }
    }

    public static void assertGlobalSubscribe(TestPresenter presenter, boolean expected) {
        try {
            presenter.subscribe(Single.just(true), new ViewNotifier<VisumView, Boolean>() {

                @Override
                public void notifyCompleted(VisumView visumView) {}

                @Override
                public void notifyResult(VisumView view, Boolean aBoolean) {}

                @Override
                public void notifyError(VisumView view, Throwable e) {}

            });
            Assert.assertTrue("subscribe() should work here", expected);
        } catch (Exception e) {
            Assert.assertFalse("subscribe() shouldn't work here", expected);
        }
    }

}
