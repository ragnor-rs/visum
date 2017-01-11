package io.reist.visum.presenter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import org.mockito.InOrder;
import org.mockito.Mockito;

import io.reist.visum.view.VisumView;

/**
 * Created by Reist on 26.05.16.
 */
public class TestPresenter extends VisumPresenter<VisumView> implements PresenterAssert.AssertTestPresenter{

    private final TestPresenter dummy = Mockito.mock(TestPresenter.class);

    @Override
    public void onStop() {
        super.onStop();
        dummy.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
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

    @Override
    public void assertPresenterAttached(int viewId, VisumView view) {
        InOrder inOrder = Mockito.inOrder(dummy);
        inOrder.verify(dummy, Mockito.times(1)).onViewAttached(viewId, view);
        if (getViewCount() == 0) {
            inOrder.verify(dummy, Mockito.times(1)).onStart();
        }
    }

    @Override
    public void assertPresenterDetached(int viewId, VisumView view) {
        InOrder inOrder = Mockito.inOrder(dummy);
        inOrder.verify(dummy, Mockito.times(1)).onViewDetached(viewId, view);
        if (getViewCount() == 0) {
            inOrder.verify(dummy, Mockito.times(1)).onStop();
        }
    }
}
