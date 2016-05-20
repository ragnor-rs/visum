package io.reist.visum.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Extend your {@link FrameLayout}s with this class to take advantage of Visum MVP.
 *
 * Created by Defuera on 29/01/16.
 */
@SuppressWarnings("unused")
public abstract class VisumWidget<P extends VisumPresenter>
        extends FrameLayout
        implements VisumView<P> {

    private final VisumViewHelper viewHelper;

    private static final String ARG_STATE_SUPER = VisumWidget.class.getName() + ".ARG_STATE_SUPER";

    public VisumWidget(int viewId, Context context) {
        super(context);
        this.viewHelper = new VisumViewHelper(viewId, new VisumClientHelper(this));
    }

    public VisumWidget(int viewId, Context context, AttributeSet attrs) {
        super(context, attrs);
        this.viewHelper = new VisumViewHelper(viewId, new VisumClientHelper(this));
    }

    /**
     * @deprecated use {@link #VisumWidget(int, Context)} instead
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public VisumWidget(Context context) {
        this(VisumPresenter.VIEW_ID_DEFAULT, context);
    }

    /**
     * @deprecated use {@link #VisumWidget(int, Context, AttributeSet)} instead
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public VisumWidget(Context context, AttributeSet attrs) {
        this(VisumPresenter.VIEW_ID_DEFAULT, context, attrs);
    }


    //region VisumClient implementation

    @Override
    public final Long getComponentId() {
        return viewHelper.getComponentId();
    }

    @Override
    public final void setComponentId(Long componentId) {
        viewHelper.setComponentId(componentId);
    }

    @Override
    public final Object getComponent() {
        return viewHelper.getComponent();
    }

    @Override
    public final ComponentCache getComponentCache() {
        return isInEditMode() ? null : viewHelper.getComponentCache(getContext());
    }

    @Override
    public void onInvalidateComponent() {
        viewHelper.onInvalidateComponent();
    }

    //endregion


    //region VisumView implementation

    @Override
    public void attachPresenter() {
        viewHelper.attachPresenter();
    }

    @Override
    public void detachPresenter() {
        viewHelper.detachPresenter();
    }

    //endregion


    //region View implementation

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewHelper.onCreate();
        inflate(getContext(), getLayoutRes(), this);
        viewHelper.onResume();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        viewHelper.onPause();
        viewHelper.onDestroy();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        viewHelper.onSaveInstanceState(bundle);
        bundle.putParcelable(ARG_STATE_SUPER, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            viewHelper.onRestoreInstanceState(bundle);
            state = bundle.getParcelable(ARG_STATE_SUPER);
        }
        super.onRestoreInstanceState(state);
    }

    //endregion


    @LayoutRes
    protected abstract int getLayoutRes();

}
