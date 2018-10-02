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

package io.reist.sandbox.repos.model.remote;

import java.util.List;

import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.remote.RetrofitService;
import io.reist.sandbox.app.model.remote.SandboxApi;
import io.reist.sandbox.repos.model.RepoService;
import rx.Observable;
import rx.functions.Action1;

public class RetrofitRepoService extends RetrofitService<Repo> implements RepoService {

    public RetrofitRepoService(SandboxApi sandboxApi) {
        super(sandboxApi);
    }

    @Override
    public Observable<SandboxResponse<List<Repo>>> list() {
        return sandboxApi.listRepos();
    }

    @Override
    public Observable<SandboxResponse<Repo>> byId(Long id) {
        return sandboxApi.repoById(id);
    }

    @Override
    public Observable<SandboxResponse<List<Repo>>> save(List<Repo> list) { //cur this is not what we really get form api
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<SandboxResponse<Repo>> save(Repo repo) {
        return sandboxApi.save(repo).doOnNext(r -> notifyDataChanged(repo));
    }

    @Override
    public Observable<SandboxResponse<Integer>> delete(Long id) {
        return sandboxApi.deleteRepo(id);
    }

    @Override
    public SandboxResponse<List<Repo>> saveSync(List<Repo> list) {
        throw new UnsupportedOperationException("you cannot save make api calls synchronously");
    }

    @Override
    public SandboxResponse<Repo> saveSync(Repo repo) {
        throw new UnsupportedOperationException("you cannot save make api calls synchronously");
    }

    @Override
    public Observable<SandboxResponse<Repo>> unlike(Repo repo) {
        return sandboxApi.unlike(repo.id);
    }

    @Override
    public Observable<SandboxResponse<Repo>> like(Repo repo) {
        return sandboxApi.like(repo.id);
    }

    @Override
    public Observable<SandboxResponse<List<Repo>>> findReposByUserId(Long userId) {
        return sandboxApi.reposByUserId(userId);
    }

}
