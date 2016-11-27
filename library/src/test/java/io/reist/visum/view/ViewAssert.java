package io.reist.visum.view;

import org.mockito.InOrder;
import org.mockito.Mockito;

import io.reist.visum.presenter.BasePresenter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Reist on 07.06.16.
 */
public class ViewAssert {

    public static void assertPresenterReattached(VisumView oldTestView, VisumView newTestView) {

        assertFalse("View has not been recreated", newTestView == oldTestView);

        BasePresenter oldPresenter = oldTestView.getPresenter();
        BasePresenter newPresenter = newTestView.getPresenter();
        assertNotNull("No presenter is attached to the new instance of the view", newPresenter);
        assertTrue("Presenter didn't survive", oldPresenter == newPresenter);

    }

    public static void assertPresenterAttachedBeforeOnActivityResult(VisumResultReceiver view) {
        VisumResultReceiver dummy = view.getDummy();
        InOrder inOrder = Mockito.inOrder(dummy);
        inOrder.verify(dummy, Mockito.times(2)).attachPresenter(); // 2 because attachPresenter was already called after onCreate
        inOrder.verify(dummy, Mockito.times(1)).onActivityResult();
    }

}
