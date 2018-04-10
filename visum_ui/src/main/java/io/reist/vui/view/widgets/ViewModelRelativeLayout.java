package io.reist.vui.view.widgets;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reist.visum.view.ViewModelWidget;
import io.reist.visum.model.ViewModel;

/**
 * Created by Reist on 09.03.17.
 */

public abstract class ViewModelRelativeLayout<VM extends ViewModel>
        extends RelativeLayout
        implements ViewModelWidget<VM> {

    private VM viewModel;

    private Unbinder unbinder;

    public ViewModelRelativeLayout(Context context) {
        super(context);
        inflate();
        init(null);
    }

    public ViewModelRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate();
        init(attrs);
    }

    public ViewModelRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate();
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
    }

    @Override
    protected final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unbindUiElements();
    }

    @CallSuper
    @Override
    public void init(@Nullable AttributeSet attributeSet) {}

    protected final void inflate() {
        LayoutInflater.from(getContext()).inflate(getLayoutRes(), this, true);
        bindUiElements();
    }

    @Override
    public final void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!isInEditMode()) {
            super.addView(child, index, params);
        }
    }

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

    protected abstract int getLayoutRes();

}
