package io.reist.visum;

import android.content.Context;
import android.support.annotation.CallSuper;

/**
 * A helper class for implementations of {@link VisumClient}. It provides callback for typical
 * Android components such as {@link android.app.Service}.
 *
 * @param <C>   Visum client type
 *
 * Created by Reist on 19.05.16.
 */
public class VisumClientHelper<C extends VisumClient> {

    protected final C client;

    protected Long componentId;

    public VisumClientHelper(C client) {
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

    @CallSuper
    public void onCreate() {
        client.inject(client.getComponent());
    }

    @CallSuper
    public void onDestroy() {
        client.onInvalidateComponent();
    }

    @CallSuper
    public void onInvalidateComponent() {
        client.getComponentCache().invalidateComponentFor(client);
    }

}
