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

package io.reist.sandbox.users.model.remote;

import java.util.List;

import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.app.model.remote.RetrofitService;
import io.reist.sandbox.app.model.remote.SandboxApi;
import io.reist.sandbox.users.model.UserService;
import rx.Observable;

/**
 * Created by m039 on 11/12/15.
 */
public class RetrofitUserService extends RetrofitService<User> implements UserService {

    public RetrofitUserService(SandboxApi sandboxApi) {
        super(sandboxApi);
    }

    @Override
    public Observable<SandboxResponse<List<User>>> list() {
        return sandboxApi.listUsers();
    }

    @Override
    public SandboxResponse<User> saveSync(User user) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public Observable<SandboxResponse<User>> byId(Long id) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public Observable<SandboxResponse<Integer>> delete(Long id) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public SandboxResponse<List<User>> saveSync(List<User> list) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public Observable<SandboxResponse<List<User>>> save(List<User> list) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public Observable<SandboxResponse<User>> save(User t) {
        throw new UnsupportedOperationException("Unsupported");
    }

}
