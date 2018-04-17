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

package io.reist.sandbox.users;

import com.pushtorefresh.storio2.sqlite.StorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reist.sandbox.app.model.remote.SandboxApi;
import io.reist.sandbox.users.model.CachedUserService;
import io.reist.sandbox.users.model.UserService;
import io.reist.sandbox.users.model.local.StorIoUserService;
import io.reist.sandbox.users.model.remote.RetrofitUserService;

/**
 * Created by m039 on 11/12/15.
 */
@Module
public class UsersModule {

    @Singleton
    @Provides
    protected UserService userService(SandboxApi sandboxApi, StorIOSQLite storIOSQLite) {
        return new CachedUserService(new StorIoUserService(storIOSQLite), new RetrofitUserService(sandboxApi));
    }

}
