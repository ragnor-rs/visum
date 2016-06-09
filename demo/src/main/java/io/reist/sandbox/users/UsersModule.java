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

package io.reist.sandbox.users;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

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
