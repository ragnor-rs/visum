package io.reist.visum.presenter;

import android.support.annotation.NonNull;

import org.mockito.Mockito;

import io.reist.visum.view.VisumView;

/**
 * Created by 4xes on 17.12.16.
 */
public class TestSingleViewPresenter extends SingleViewPresenter<VisumView> {

    private final TestSingleViewPresenter dummy = Mockito.mock(TestSingleViewPresenter.class);

    @Override
    protected void onViewAttached(@NonNull VisumView view) {
        dummy.onViewAttached(view);
    }

    @Override
    protected void onViewDetached(@NonNull VisumView view) {
        dummy.onViewDetached(view);
    }

    public TestSingleViewPresenter getDummy() {
        return dummy;
    }
}
