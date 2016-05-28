package io.reist.visum;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * A helper class for implementations of {@link VisumClient}. It provides callback for typical
 * Android components such as {@link android.app.Service}.
 *
 * Created by Reist on 19.05.16.
 */
public final class VisumClientHelper<C extends VisumClient> {

    protected final C client;

    public VisumClientHelper(@NonNull C client) {
        this.client = client;
    }

    public C getClient() {
        return client;
    }

    @NonNull
    public ComponentCache getComponentCache() {
        return ((ComponentCacheProvider) client.getContext().getApplicationContext()).getComponentCache();
    }

    public void onCreate() {
        client.inject(client.getComponentCache().start(client));
    }

    public void onDestroy() {
        client.getComponentCache().stop(client);
    }

    @NonNull
    public Context getContext() {
        return client.getContext();
    }

    public void onRestartClient() {
       client.inject(client.getComponentCache().findComponentEntryByClientOrThrow(client).component);
    }

}
