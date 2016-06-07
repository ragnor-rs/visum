package io.reist.visum.presenter;

import android.support.annotation.NonNull;

import org.mockito.Mockito;

import io.reist.visum.view.VisumView;

/**
 * Created by Reist on 26.05.16.
 */
public class TestPresenter extends VisumPresenter<VisumView> {

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

    public TestPresenter getDummy() {
        return dummy;
    }

}
