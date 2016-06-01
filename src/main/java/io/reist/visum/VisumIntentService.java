package io.reist.visum;

import android.app.IntentService;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Extend your {@link IntentService}s with this class to take advantage of Visum MVP.
 *
 * Created by Defuera on 2/2/16.
 */
public abstract class VisumIntentService extends IntentService implements VisumClient {

    private final VisumClientHelper helper = new VisumClientHelper<>(this);

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public VisumIntentService(String name) {
        super(name);
    }


    //region VisumClient implementation

    @Override
    public final void onStartClient() {
        helper.onCreate();
    }

    @NonNull
    @Override
    public final ComponentCache getComponentCache() {
        return helper.getComponentCache();
    }

    @Override
    public final void onStopClient() {
        helper.onDestroy(false);
    }

    @NonNull
    @Override
    public Context getContext() {
        return this;
    }

    //endregion


    //region Service implementation

    @Override
    public void onCreate() {
        super.onCreate();
        onStartClient();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onStopClient();
    }

    //endregion


}

