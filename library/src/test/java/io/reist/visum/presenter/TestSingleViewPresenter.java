package io.reist.visum.presenter;

import android.support.annotation.NonNull;

import org.mockito.InOrder;
import org.mockito.Mockito;

import io.reist.visum.view.VisumView;

/**
 * Created by 4xes on 17.12.16.
 */
public class TestSingleViewPresenter extends SingleViewPresenter<VisumView> implements PresenterAssert.AssertTestPresenter {

    private final TestSingleViewPresenter dummy = Mockito.mock(TestSingleViewPresenter.class);

    @Override
    protected void onViewAttached(@NonNull VisumView view) {
        dummy.onViewAttached(view);
        super.onViewAttached(view);
    }

    @Override
    protected void onViewDetached(@NonNull VisumView view) {
        dummy.onViewDetached(view);
        super.onViewAttached(view);
    }

    @Override
    public void assertPresenterAttached(int viewId, VisumView view) {
        InOrder inOrder = Mockito.inOrder(dummy);
        inOrder.verify(dummy, Mockito.times(1)).onViewAttached(view);
    }

    @Override
    public void assertPresenterDetached(int viewId, VisumView view) {
        InOrder inOrder = Mockito.inOrder(dummy);
        inOrder.verify(dummy, Mockito.times(1)).onViewDetached(view);
    }

}
