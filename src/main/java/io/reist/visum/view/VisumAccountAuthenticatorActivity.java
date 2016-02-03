package io.reist.visum.view;

import android.accounts.AccountAuthenticatorActivity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;

import butterknife.ButterKnife;
import io.reist.visum.ComponentCache;
import io.reist.visum.ComponentCacheProvider;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by defuera on 01/02/2016.
 * If you need implement AccountAuthenticatorActivity and still want to get Visum benefits,
 * here you go.
 */
public abstract class VisumAccountAuthenticatorActivity<P extends VisumPresenter>
        extends AccountAuthenticatorActivity
        implements VisumView<P>, VisumClient {

    private static final String ARG_STATE_COMPONENT_ID = "ARG_STATE_COMPONENT_ID";

    private Long componentId;
    private boolean stateSaved;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inject(getComponent());

        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        attachPresenter();
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @SuppressWarnings("unchecked")
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!stateSaved) {
            getComponentCache().invalidateComponentFor(this);
        }
        detachPresenter();
    }

    private ComponentCache getComponentCache() {
        ComponentCacheProvider application = (ComponentCacheProvider) getApplicationContext();
        return application.getComponentCache();
    }


    //region VisumView

    @SuppressWarnings("unchecked") //todo setView should be checked call
    @Override
    public void attachPresenter() {
        final P presenter = getPresenter();
        if (presenter != null) {
            presenter.setView(this);
        }
    }

    @SuppressWarnings("unchecked") //todo setView should be type safe call
    @Override
    public void detachPresenter() {
        if (getPresenter() != null)
            getPresenter().setView(null);
    }

    //endregion

    //region VisumClient

    @Override
    public Long getComponentId() {
        return componentId;
    }

    @Override
    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    @Override
    public Object getComponent() {
        if (getComponentCache() != null) {
            return getComponentCache().getComponentFor(this);
        } else {
            return null;
        }
    }

    //endregion


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        stateSaved = true;

        Bundle bundle = new Bundle();
        bundle.putLong(ARG_STATE_COMPONENT_ID, componentId);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            componentId = savedInstanceState.getLong(ARG_STATE_COMPONENT_ID);
        }

        stateSaved = false;
    }
}
