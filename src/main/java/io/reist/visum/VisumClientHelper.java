package io.reist.visum;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * A helper class for implementations of {@link VisumClient}. It provides callback for typical
 * Android components such as {@link android.app.Service}.
 *
 * Created by Reist on 19.05.16.
 */
public final class VisumClientHelper {

    protected final VisumClient client;

    public VisumClientHelper(@NonNull VisumClient client) {
        this.client = client;
    }

    @NonNull
    public Object onStartClient() {
        return client.getComponentCache().onStartClient(client);
    }

    @NonNull
    public ComponentCache getComponentCache(Context context) {
        return ((ComponentCacheProvider) context.getApplicationContext()).getComponentCache();
    }

    public void onCreate() {
        client.inject(client.onStartClient());
    }

    public void onDestroy() {
        client.onStopClient();
    }

    public void onStopClient() {
        client.getComponentCache().onStopClient(client);
    }

    @NonNull
    public VisumClient getClient() {
        return client;
    }

}
