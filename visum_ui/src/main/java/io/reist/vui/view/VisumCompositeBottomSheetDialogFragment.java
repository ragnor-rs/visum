package io.reist.vui.view;

import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reist.visum.presenter.VisumPresenter;
import io.reist.visum.view.VisumBottomSheetDialogFragment;

/**
 * Created by Reist on 15.03.17.
 */
public abstract class VisumCompositeBottomSheetDialogFragment<P extends VisumPresenter>
        extends VisumBottomSheetDialogFragment<P> {

    private Unbinder unbinder;

    @Override
    protected final void bindViews(View view) {
        if (unbinder == null && view != null) {
            unbinder = ButterKnife.bind(this, view);
        }
    }

    @Override
    public final void unbindUiElements() {
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

}
