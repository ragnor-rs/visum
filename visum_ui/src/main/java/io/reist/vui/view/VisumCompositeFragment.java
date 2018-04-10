package io.reist.vui.view;

import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reist.visum.presenter.VisumPresenter;
import io.reist.visum.view.VisumFragment;

/**
 * Created by Reist on 15.03.17.
 */
public abstract class VisumCompositeFragment<P extends VisumPresenter>
        extends VisumFragment<P> {

    private Unbinder unbinder;

    public VisumCompositeFragment() {
        super();
    }

    public VisumCompositeFragment(int viewId) {
        super(viewId);
    }

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
