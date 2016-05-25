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

    private final VisumViewHelper helper;

    private static final String ARG_STATE_SUPER = VisumWidget.class.getName() + ".ARG_STATE_SUPER";

    public VisumWidget(int viewId, Context context) {
        super(context);
        this.helper = new VisumViewHelper(viewId, new VisumClientHelper(this));
    }

    public VisumWidget(int viewId, Context context, AttributeSet attrs) {
        super(context, attrs);
        this.helper = new VisumViewHelper(viewId, new VisumClientHelper(this));
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
        return helper.getComponentId();
    }

    @Override
    public final void setComponentId(Long componentId) {
        helper.setComponentId(componentId);
    }

    @Override
    public final Object getComponent() {
        return helper.getComponent();
    }

    @Override
    public final ComponentCache getComponentCache() {
        return isInEditMode() ? null : helper.getComponentCache(getContext());
    }

    @Override
    public void onInvalidateComponent() {
        helper.onInvalidateComponent();
    }

    //endregion


    //region VisumView implementation

    @Override
    public void attachPresenter() {
        helper.attachPresenter();
    }

    @Override
    public void detachPresenter() {
        helper.detachPresenter();
    }

    @Override
    public int getViewId() {
        return helper.getViewId();
    }

    //endregion


    //region View implementation

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        helper.onCreate();
        inflate(getContext(), getLayoutRes(), this);
        helper.onResume();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        helper.onPause();
        helper.onDestroy();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        helper.onSaveInstanceState(bundle);
        bundle.putParcelable(ARG_STATE_SUPER, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            helper.onRestoreInstanceState(bundle);
            state = bundle.getParcelable(ARG_STATE_SUPER);
        }
        super.onRestoreInstanceState(state);
    }

    //endregion


    @LayoutRes
    protected abstract int getLayoutRes();

}
