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

package io.reist.sandbox.app.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by m039 on 11/26/15.
 * This is a helper interface for your api responses.
 * If you use REST api and all your responses are wrapped in some model.
 * This is probably the way to go.
 */
public class SandboxResponse<T> {

    @SerializedName("result")
    private T result;

    @SerializedName("error")
    private SandboxError error;

    public SandboxResponse(T result) {
        this.result = result;
    }

    public SandboxResponse(SandboxError error) {
        this.error = error;
    }

    @Nullable
    public T getResult() {
        return result;
    }

    @Nullable
    public SandboxError getError() {
        return error;
    }

    public boolean isSuccessful() {
        return error == null;
    }

}
