/*
 * Copyright (C) 2017 Renat Sarymsakov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.reist.visum;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * A helper class for implementations of {@link VisumClient}. It provides callback for typical
 * Android components such as {@link android.app.Service}.
 *
 * Created by Reist on 19.05.16.
 */
public final class VisumClientHelper<C extends VisumClient> {

    private static final String TAG = VisumClientHelper.class.getSimpleName();

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
        Object component = client.getComponentCache().start(client);
        if (component != null) {
            client.inject(component);
        } else {
            Log.w(TAG, "No component for " + client);
        }
    }

    public void onDestroy(boolean retainComponent) {
        client.getComponentCache().stop(client, retainComponent);
    }

    @NonNull
    public Context getContext() {
        return client.getContext();
    }

}
