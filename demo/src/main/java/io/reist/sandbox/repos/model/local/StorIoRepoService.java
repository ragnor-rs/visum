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

package io.reist.sandbox.repos.model.local;

import com.pushtorefresh.storio2.sqlite.StorIOSQLite;
import com.pushtorefresh.storio2.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio2.sqlite.queries.Query;

import java.util.List;

import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.local.StorIoService;
import io.reist.sandbox.repos.model.RepoService;
import rx.Observable;

public class StorIoRepoService extends StorIoService<Repo> implements RepoService {

    public StorIoRepoService(StorIOSQLite storIoSqLite) {
        super(storIoSqLite);
    }

    @Override
    public Observable<SandboxResponse<List<Repo>>> list() {
        return preparedGetBuilder(Repo.class)
                .withQuery(Query.builder().table(RepoTable.NAME).build())
                .prepare()
                .asRxObservable()
                .map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<Repo>> byId(Long id) {
        return unique(Repo.class, RepoTable.NAME, id)
                .map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<Integer>> delete(Long id) {
        return storIoSqLite        //cur if that's fine to make storIoSqLite protected?
                .delete()
                .byQuery(
                        DeleteQuery.builder()
                                .table(RepoTable.NAME)
                                .where(RepoTable.Column.ID + " = ?")
                                .whereArgs(id)
                                .build()
                )
                .prepare() // BTW: it will use transaction!
                .asRxObservable()
                .map(t -> new SandboxResponse<>(t.numberOfRowsDeleted()));
    }

    @Override
    public Observable<SandboxResponse<List<Repo>>> findReposByUserId(Long userId) {
        return preparedGetBuilder(Repo.class)
                .withQuery(
                        Query
                                .builder()
                                .table(RepoTable.NAME)
                                .where(RepoTable.Column.USER_ID + " = ?")
                                .whereArgs(userId)
                                .orderBy(RepoTable.Column.ID)
                                .build())
                .prepare()
                .asRxObservable()
                .map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<Repo>> unlike(Repo repo) {
        return like(repo, false);
    }

    @Override
    public Observable<SandboxResponse<Repo>> like(Repo repo) {
        return like(repo, true);
    }

    private Observable<SandboxResponse<Repo>> like(final Repo repo, boolean likedByMe) {

        if (likedByMe) {
            repo.likeCount += 1;
        } else {
            repo.likeCount -= 1;
        }

        repo.likedByMe = likedByMe;

        preparedPutBuilder()
                .object(repo)
                .prepare()
                .executeAsBlocking();

        return Observable.just(repo).map(SandboxResponse::new);

    }

}
