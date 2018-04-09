package io.reist.vui.view.widgets;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reist.vui.model.ViewModel;

/**
 * Created by Reist on 09.03.17.
 */

public abstract class ViewModelImageView<VM extends ViewModel>
        extends AppCompatImageView
        implements ViewModelWidget<VM> {

    private VM viewModel;

    private Unbinder unbinder;

    public ViewModelImageView(Context context) {
        super(context);
        bindUiElements();
        init(null);
    }

    public ViewModelImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bindUiElements();
        init(attrs);
    }

    public ViewModelImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bindUiElements();
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

    @Override
    public final void rebindUiElements() {
        unbindUiElements();
        bindUiElements();
    }

    @Override
    protected final void onAttachedToWindow() {
        super.onAttachedToWindow();
        bindUiElements();
        onWidgetShow();
    }

    @Override
    protected final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onWidgetHide();
        unbindUiElements();
    }

    @CallSuper
    @Override
    public void onWidgetShow() {}

    @CallSuper
    @Override
    public void onWidgetHide() {}

    @CallSuper
    @Override
    public void init(@Nullable AttributeSet attributeSet) {}

    @Override
    public final VM getViewModel() {
        return viewModel;
    }

    @CallSuper
    @Override
    public void bindViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
        bindUiElements();
    }

}
