/*
 * Copyright (c) 2015  Zvooq LTD.
 * Authors: Renat Sarymsakov, Dmitriy Mozgin, Denis Volyntsev.
 *
 * This file is part of MVP-Sandbox.
 *
 * MVP-Sandbox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MVP-Sandbox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MVP-Sandbox.  If not, see <http://www.gnu.org/licenses/>.
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
