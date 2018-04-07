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
 * Created by Reist on 26.05.16.
 */
public abstract class BaseTestClient implements VisumClient {

    private final ComponentCache componentCache;

    protected BaseTestClient(ComponentCache componentCache) {
        this.componentCache = componentCache;
    }

    @Override
    public ComponentCache getComponentCache() {
        return componentCache;
    }

    @Override
    public void onStartClient() {
        componentCache.start(this);
    }

    @Override
    public void onStopClient() {
        componentCache.stop(this, false);
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void inject(Object component) {}

}
