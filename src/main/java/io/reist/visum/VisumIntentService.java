package io.reist.visum;

import android.app.IntentService;

/**
 * Created by defuera on 02/02/2016.
 */
public abstract class VisumIntentService extends IntentService implements VisumClient {

    private final VisumClientHelper<VisumIntentService> clientHelper = new VisumClientHelper<>(this);

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public VisumIntentService(String name) {
        super(name);
    }


    //region Service implementation

    @Override
    public void onCreate() {
        super.onCreate();
        clientHelper.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clientHelper.onDestroy();
    }

    //endregion


    //region VisumClient

    @Override
    public Long getComponentId() {
        return clientHelper.getComponentId();
    }

    @Override
    public void setComponentId(Long componentId) {
        clientHelper.setComponentId(componentId);
    }

    @Override
    public Object getComponent() {
        return clientHelper.getComponent();
    }

    @Override
    public ComponentCache getComponentCache() {
        return clientHelper.getComponentCache(this);
    }

    public void onInvalidateComponent() {
        clientHelper.onInvalidateComponent();
    }

    //endregion


}

