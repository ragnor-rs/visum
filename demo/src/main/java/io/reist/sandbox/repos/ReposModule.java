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

package io.reist.sandbox.repos;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reist.sandbox.app.SandboxModule;
import io.reist.sandbox.app.model.remote.SandboxApi;
import io.reist.sandbox.repos.model.CachedRepoService;
import io.reist.sandbox.repos.model.RepoService;
import io.reist.sandbox.repos.model.local.StorIoRepoService;
import io.reist.sandbox.repos.model.remote.RetrofitRepoService;

/**
 * Created by Reist on 29.11.15.
 */
@Module
public class ReposModule {

    @Provides @Singleton @Named(SandboxModule.LOCAL_SERVICE)
    protected RepoService localRepoService(StorIOSQLite storIoSqLite) {
        return new StorIoRepoService(storIoSqLite);
    }

    @Provides @Singleton @Named(SandboxModule.REMOTE_SERVICE)
    protected RepoService remoteRepoService(SandboxApi sandboxApi) {
        return new RetrofitRepoService(sandboxApi);
    }

    @Provides @Singleton
    protected RepoService repoService(
            @Named(SandboxModule.LOCAL_SERVICE) RepoService local,
            @Named(SandboxModule.REMOTE_SERVICE) RepoService remote
    ) {
        return new CachedRepoService(local, remote);
    }

}
