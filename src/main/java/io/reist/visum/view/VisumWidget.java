package io.reist.visum.view;

import android.content.Context;
import android.support.annotation.CallSuper;
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

    private final VisumViewHelper<P> helper;

    public VisumWidget(int viewId, Context context) {
        super(context);
        this.helper = new VisumViewHelper<>(viewId, new VisumClientHelper<>(this));
    }

    public VisumWidget(int viewId, Context context, AttributeSet attrs) {
        super(context, attrs);
        this.helper = new VisumViewHelper<>(viewId, new VisumClientHelper<>(this));
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
    public final void onStartClient() {
        helper.onCreate();
    }

    @Override
    public final ComponentCache getComponentCache() {
        return isInEditMode() ? null : helper.getComponentCache();
    }

    @Override
    public final void onStopClient() {
        helper.onDestroy();
    }

    //endregion


    //region VisumView implementation

    @Override
    @CallSuper
    public void attachPresenter() {
        helper.attachPresenter();
    }

    @Override
    @CallSuper
    public void detachPresenter() {
        helper.detachPresenter();
    }

    //endregion


    //region View implementation

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        inflate();
        helper.onCreate();
        attachPresenter();
    }

    protected void inflate() {
        inflate(getContext(), getLayoutRes(), this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        detachPresenter();
        helper.onDestroy();
    }

    //endregion


    @LayoutRes
    protected abstract int getLayoutRes();

}
