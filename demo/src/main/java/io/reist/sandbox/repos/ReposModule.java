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
