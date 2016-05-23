package io.reist.visum;

import android.content.Context;
import android.support.annotation.CallSuper;

/**
 * Any non-UI class which utilizes Visum dependency injection mechanism.
 *
 * @see VisumClient
 */
public abstract class VisumBaseClient implements VisumClient {

    private final VisumClientHelper helper;
    private final Context context;

    public VisumBaseClient(Context context) {
        this.helper = new VisumClientHelper(this);
        this.context = context.getApplicationContext();
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
        return helper.getComponentCache(context);
    }

    @Override
    @CallSuper
    public void onInvalidateComponent() {
        helper.onInvalidateComponent();
    }

    //endregion


    protected final VisumClientHelper getClientHelper() {
        return helper;
    }

    protected final Context getContext() {
        return context;
    }

}
