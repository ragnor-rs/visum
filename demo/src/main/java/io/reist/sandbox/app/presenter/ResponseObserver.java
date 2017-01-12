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

package io.reist.sandbox.app.presenter;

import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.model.SandboxResponse;
import rx.Observer;

/**
 * Created by defuera on 12/11/2015.
 */
public abstract class ResponseObserver<T> implements Observer<SandboxResponse<T>> {

    @Override
    public void onNext(SandboxResponse<T> response) {
        if (response.isSuccessful())
            onSuccess(response.getResult());
        else
            onFail(response.getError());
    }

    protected abstract void onFail(SandboxError error);

    protected abstract void onSuccess(T result);

    @Override
    public void onError(Throwable e) {
        onFail(new SandboxError(e));
    }

    @Override
    public void onCompleted() {}

}
