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
        helper.onDestroy(false);
    }

    @NonNull
    public final Context getContext() {
        return context;
    }

    //endregion


}
