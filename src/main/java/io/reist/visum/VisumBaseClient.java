package io.reist.visum;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Any non-UI class which utilizes Visum dependency injection mechanism.
 *
 * @see VisumClient
 */
public abstract class VisumBaseClient implements VisumClient {

    private final VisumClientHelper helper;
    private final Context context;

    public VisumBaseClient(@NonNull Context context) {
        this.helper = new VisumClientHelper<>(this);
        this.context = context.getApplicationContext();
    }


    //region VisumClient implementation

    @NonNull
    @Override
    public final ComponentCache getComponentCache() {
        return helper.getComponentCache();
    }

    @Override
    public final void onStartClient() {
        helper.onCreate();
    }

    @Override
    public final void onStopClient() {
        helper.onDestroy();
    }

    @NonNull
    public final Context getContext() {
        return context;
    }

    //endregion


}
