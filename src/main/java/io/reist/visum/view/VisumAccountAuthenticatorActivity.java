package io.reist.visum.view;

import android.accounts.AccountAuthenticatorActivity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;

import io.reist.visum.ComponentCache;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Extend this class if you need to implement AccountAuthenticatorActivity and you want to
 * benefit from Visum.
 *
 * Created by defuera on 01/02/2016.
 */
public abstract class VisumAccountAuthenticatorActivity<P extends VisumPresenter>
        extends AccountAuthenticatorActivity
        implements VisumView<P> {

    private final VisumViewHelper viewHelper;

    /**
     * @deprecated use {@link #VisumAccountAuthenticatorActivity(int)} instead
     */
    @Deprecated
    public VisumAccountAuthenticatorActivity() {
        this(VisumView.VIEW_ID_DEFAULT);
    }

    public VisumAccountAuthenticatorActivity(int viewId) {
        this.viewHelper = new VisumViewHelper(viewId, this);
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
        return viewHelper.getComponentCache(this);
    }

    //endregion


    //region VisumView implementation

    @Override
    public void onInvalidateComponent() {
        viewHelper.onInvalidateComponent();
    }

    @Override
    public void attachPresenter() {
        viewHelper.attachPresenter();
    }

    @Override
    public void detachPresenter() {
        viewHelper.detachPresenter();
    }

    //endregion


    //region Activity implementation

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewHelper.onCreate();
        viewHelper.onRestoreInstanceState(savedInstanceState);
        setContentView(getLayoutRes());
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewHelper.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        viewHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewHelper.onDestroy();
    }

    //endregion


    @LayoutRes
    protected abstract int getLayoutRes();

}
