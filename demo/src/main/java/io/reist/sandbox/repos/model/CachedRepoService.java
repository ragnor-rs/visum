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

package io.reist.sandbox.repos.model;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.List;

import io.reist.sandbox.app.model.CachedService;
import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.SandboxResponse;
import rx.Observable;

public class CachedRepoService extends CachedService<Repo> implements RepoService {

    protected final RepoService local;
    protected final RepoService remote;

    public CachedRepoService(RepoService local, RepoService remote) {
        super(local, remote);

        this.local = local;
        this.remote = remote;
    }

    @RxLogObservable
    @Override
    public Observable<SandboxResponse<List<Repo>>> findReposByUserId(final Long userId) {
        return Observable
                .merge(
                        local.findReposByUserId(userId),
                        remote.findReposByUserId(userId).compose(new SaveAndEmitErrorsListTransformer<>(local))
                )
                .filter(new ListResponseFilter<>());
    }

    @RxLogObservable
    @Override
    public Observable<SandboxResponse<Repo>> like(Repo repo) {
        return like(repo, true);
    }

    @RxLogObservable
    @Override
    public Observable<SandboxResponse<Repo>> unlike(Repo repo) {
        return like(repo, false);
    }

    @RxLogObservable
    private Observable<SandboxResponse<Repo>> like(Repo repo, boolean like) {
        return Observable
                .merge(
                        (like ? local.like(repo) : local.unlike(repo)),
                        (like ? remote.like(repo) : remote.unlike(repo)
                )
                .compose(new SaveAndEmitErrorsTransformer<>(local)))
                .filter(new ResponseFilter<>());
    }

}
