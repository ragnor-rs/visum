package io.reist.visum.view;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.BasePresenter;
import io.reist.visum.presenter.VisumSinglePresenter;

/**
 * Extend your {@link FrameLayout}s with this class to take advantage of Visum MVP.
 *
 * Created by Defuera on 29/01/16.
 */
@SuppressWarnings("unused")
public abstract class VisumWidget<P extends BasePresenter>
        extends FrameLayout
        implements VisumView<P> {

    private final VisumViewHelper<P> helper;

    public VisumWidget(int viewId, Context context) {
        super(context);
        inflate();
        this.helper = new VisumViewHelper<>(viewId, new VisumClientHelper<>(this));
    }

    public VisumWidget(int viewId, Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate();
        this.helper = new VisumViewHelper<>(viewId, new VisumClientHelper<>(this));
    }

    /**
     * @deprecated use {@link #VisumWidget(int, Context)} instead
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public VisumWidget(Context context) {
        this(VisumSinglePresenter.VIEW_ID_DEFAULT, context);
    }

    /**
     * @deprecated use {@link #VisumWidget(int, Context, AttributeSet)} instead
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public VisumWidget(Context context, AttributeSet attrs) {
        this(VisumSinglePresenter.VIEW_ID_DEFAULT, context, attrs);
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
        helper.onDestroy(false);
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
        if (!isInEditMode()){
            onStartClient();
        }
        attachPresenter();
    }

    protected void inflate() {
        inflate(getContext(), getLayoutRes(), this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        detachPresenter();
        onStopClient();
    }

    //endregion


    @LayoutRes
    protected abstract int getLayoutRes();

}
