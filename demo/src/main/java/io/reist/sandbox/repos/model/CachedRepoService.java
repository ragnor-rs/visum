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
