package io.reist.visum;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

public abstract class VisumTaskService extends GcmTaskService implements VisumClient {

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
        helper.onDestroy(false);
    }

    @NonNull
    @Override
    public Context getContext() {
        return this;
    }

    //endregion

}
