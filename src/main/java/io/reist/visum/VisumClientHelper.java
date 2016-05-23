package io.reist.visum;

import android.content.Context;

/**
 * A helper class for implementations of {@link VisumClient}. It provides callback for typical
 * Android components such as {@link android.app.Service}.
 *
 * Created by Reist on 19.05.16.
 */
public final class VisumClientHelper {

    protected final VisumClient client;

    protected Long componentId;

    public VisumClientHelper(VisumClient client) {
        this.client = client;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public Object getComponent() {
        ComponentCache componentCache = client.getComponentCache();
        if (componentCache != null) {
            return componentCache.getComponentFor(client);
        } else {
            return null;
        }
    }

    public ComponentCache getComponentCache(Context context) {
        return ((ComponentCacheProvider) context.getApplicationContext()).getComponentCache();
    }

    public void onCreate() {
        client.inject(client.getComponent());
    }

    public void onDestroy() {
        client.onInvalidateComponent();
    }

    public void onInvalidateComponent() {
        client.getComponentCache().invalidateComponentFor(client);
    }

    public VisumClient getClient() {
        return client;
    }

}
