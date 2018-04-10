package io.reist.vui.view.widgets;

import android.content.Context;
import android.util.AttributeSet;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reist.visum.model.ViewModel;
import io.reist.visum.presenter.VisumPresenter;
import io.reist.visum.view.VisumWidget;

/**
 * Created by Reist on 09.03.17.
 */

public abstract class VisumViewModelWidget<P extends VisumPresenter, VM extends ViewModel>
        extends VisumWidget<P, VM> {

    private Unbinder unbinder;

    public VisumViewModelWidget(int viewId, Context context) {
        super(viewId, context);
        init(null);
    }

    public VisumViewModelWidget(int viewId, Context context, AttributeSet attrs) {
        super(viewId, context, attrs);
        init(attrs);
    }

    public VisumViewModelWidget(Context context) {
        super(context);
        init(null);
    }

    public VisumViewModelWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

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
