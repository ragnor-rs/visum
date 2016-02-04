package io.reist.visum;

import android.app.IntentService;

import io.reist.visum.view.VisumClient;

/**
 * Created by defuera on 02/02/2016.
 */
public abstract class VisumIntentService extends IntentService implements VisumClient {

    private Long componentId;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public VisumIntentService(String name) {
        super(name);
    }

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

    //region VisumView

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

