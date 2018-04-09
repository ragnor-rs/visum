package io.reist.vui.view.widgets;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reist.visum.presenter.VisumPresenter;
import io.reist.visum.view.VisumWidget;
import io.reist.vui.model.ViewModel;
import io.reist.vui.view.PresenterListener;

/**
 * Prevents client code from overriding {@link #onDetachedFromWindow()}.
 * This is to minimize bugs regarding view attachment. For proper initialization / cleanup, use
 * {@link #attachPresenter()} and {@link #detachPresenter()}.
 *
 * Created by Reist on 09.03.17.
 */

public abstract class VisumViewModelWidget<P extends VisumPresenter, VM extends ViewModel>
        extends VisumWidget<P>
        implements ViewModelWidget<VM>, PresenterListener<P> {

    private VM viewModel;

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

    @Override
    public final void attachPresenter() {
        bindUiElements();
        super.attachPresenter();
        onPresenterAttached(getPresenter());
    }

    @Override
    public final void detachPresenter() {
        onPresenterDetached();
        super.detachPresenter();
        unbindUiElements();
    }

    @CallSuper
    @Override
    public void init(@Nullable AttributeSet attributeSet) {}

    @CallSuper
    @Override
    public void onPresenterAttached(P presenter) {}

    @CallSuper
    @Override
    public void onPresenterDetached() {}

    @Override
    protected final void inflate() {
        super.inflate();
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

}
