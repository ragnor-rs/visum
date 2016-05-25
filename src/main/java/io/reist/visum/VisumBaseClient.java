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

    public VisumBaseClient(Context context) {
        this.helper = new VisumClientHelper(this);
        this.context = context.getApplicationContext();
    }


    //region VisumClient implementation

    @NonNull
    @Override
    public final Object onStartClient() {
        return helper.onStartClient();
    }

    @NonNull
    @Override
    public final ComponentCache getComponentCache() {
        return helper.getComponentCache(context);
    }

    @Override
    public final void onStopClient() {
        helper.onStopClient();
    }

    //endregion


    protected final VisumClientHelper getClientHelper() {
        return helper;
    }

    protected final Context getContext() {
        return context;
    }

}
