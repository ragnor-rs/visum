package io.reist.visum;

import android.app.Service;

import io.reist.visum.view.VisumClient;

/**
 * Created by defuera on 02/02/2016.
 */
public abstract class VisumAndroidService extends Service implements VisumClient {

    private Long componentId;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate() {
        super.onCreate();
        inject(getComponent());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDestroy() {
        super.onDestroy();
        getComponentCache().invalidateComponentFor(this);
    }

    private ComponentCache getComponentCache() {
        ComponentCacheProvider application = (ComponentCacheProvider) getApplicationContext();
        return application.getComponentCache();
    }

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

}
