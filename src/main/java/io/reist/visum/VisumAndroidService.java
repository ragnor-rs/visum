package io.reist.visum;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Extend your {@link Service}s with this class to take advantage of Visum MVP.
 *
 * Created by Defuera on 2/2/16.
 */
public abstract class VisumAndroidService extends Service implements VisumClient {

    private final VisumClientHelper helper = new VisumClientHelper<>(this);


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
        helper.onDestroy();
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
        helper.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.onDestroy();
    }

    //endregion


}
