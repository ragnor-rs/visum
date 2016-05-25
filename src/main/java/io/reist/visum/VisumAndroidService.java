package io.reist.visum;

import android.app.Service;
import android.support.annotation.NonNull;

/**
 * Extend your {@link Service}s with this class to take advantage of Visum MVP.
 *
 * Created by Defuera on 2/2/16.
 */
public abstract class VisumAndroidService extends Service implements VisumClient {

    private final VisumClientHelper clientHelper = new VisumClientHelper(this);


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
    public Object onStartClient() {
       return clientHelper.onStartClient();
    }

    @NonNull
    @Override
    public ComponentCache getComponentCache() {
        return clientHelper.getComponentCache(this);
    }

    public void onStopClient() {
        clientHelper.onStopClient();
    }

    //endregion


}
