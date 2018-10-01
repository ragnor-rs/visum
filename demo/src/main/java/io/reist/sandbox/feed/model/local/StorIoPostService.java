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

package io.reist.sandbox.feed.model.local;

import com.pushtorefresh.storio2.sqlite.StorIOSQLite;
import com.pushtorefresh.storio2.sqlite.queries.Query;

import java.util.List;

import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.local.StorIoService;
import io.reist.sandbox.feed.model.FeedService;
import rx.Observable;

/**
 * Created by 4xes on 8/11/16.
 */
public class StorIoPostService extends StorIoService<Post> implements FeedService {

    public StorIoPostService(StorIOSQLite storIOSQLite) {
        super(storIOSQLite);
    }

    @Override
    public Observable<SandboxResponse<List<Post>>> list() {
        return preparedGetBuilder(Post.class)
                .withQuery(Query
                        .builder()
                        .table(PostTable.NAME)
                        .orderBy(PostTable.Column.ID)
                        .build())
                .prepare()
                .asRxObservable()
                .map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<Post>> byId(Long id) {
        return unique(Post.class, PostTable.NAME, id).map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<Integer>> delete(Long id) {
        throw new IllegalStateException("Unsupported");
    }
}