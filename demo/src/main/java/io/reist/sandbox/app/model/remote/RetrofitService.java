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

package io.reist.sandbox.app.model.remote;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reist.sandbox.app.model.SandboxService;
import rx.functions.Action1;

/**
 * Created by Reist on 11/2/15.
 */
public abstract class RetrofitService<T> implements SandboxService<T> {

    protected final SandboxApi sandboxApi;

    private final List<Action1<T>> listeners = new CopyOnWriteArrayList<>();

    public RetrofitService(SandboxApi sandboxApi) {
        this.sandboxApi = sandboxApi;
    }

    protected final void notifyDataChanged(T entity) {
        for (Action1<T> listener : listeners) {
            listener.call(entity);
        }
    }

    @Override
    public void addListener(Action1<T> dataListener) {
        listeners.add(dataListener);
    }

    @Override
    public void removeListener(Action1<T> dataListener) {
        listeners.remove(dataListener);
    }

}
