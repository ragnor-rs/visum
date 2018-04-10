package io.reist.vui.view;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reist.visum.presenter.VisumPresenter;
import io.reist.visum.view.VisumActivity;

/**
 * Created by Reist on 15.03.17.
 */
public abstract class VisumCompositeActivity<P extends VisumPresenter>
        extends VisumActivity<P> {

    private Unbinder unbinder;

    @Override
    public final void bindUiElements() {
        if (unbinder == null) {
            unbinder = ButterKnife.bind(this);
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
