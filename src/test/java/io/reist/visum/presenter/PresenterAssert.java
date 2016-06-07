package io.reist.visum.presenter;

import org.mockito.InOrder;
import org.mockito.Mockito;

import io.reist.visum.view.VisumView;

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
        inOrder.verify(dummy, Mockito.times(1)).onViewAttached(viewId, view);
        if (presenter.getViewCount() == 1) {
            inOrder.verify(dummy, Mockito.times(1)).onStart();
        }
    }

}
