package io.reist.visum;

import android.app.IntentService;
import android.support.annotation.NonNull;

/**
 * Extend your {@link IntentService}s with this class to take advantage of Visum MVP.
 *
 * Created by Defuera on 2/2/16.
 */
public abstract class VisumIntentService extends IntentService implements VisumClient {

    private final VisumClientHelper clientHelper = new VisumClientHelper(this);

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

    @NonNull
    @Override
    public final Object onStartClient() {
        return clientHelper.onStartClient();
    }

    @NonNull
    @Override
    public final ComponentCache getComponentCache() {
        return clientHelper.getComponentCache(this);
    }

    public final void onStopClient() {
        clientHelper.onStopClient();
    }

    //endregion


}

