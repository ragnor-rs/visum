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

package io.reist.sandbox.feed.model.remote;

import java.util.List;

import javax.inject.Inject;

import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.feed.model.FeedService;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by 4xes on 4/11/16.
 */
public class RetrofitFeedService implements FeedService {

    private final FeedServerApi feedServerApi;

    @Inject
    public RetrofitFeedService(FeedServerApi api) {
        this.feedServerApi = api;
    }

    @Override
    public Observable<SandboxResponse<List<Post>>> list() {
        return feedServerApi.posts().map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<Post>> byId(Long id) {
        return feedServerApi.post(id).map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<List<Post>>> save(List<Post> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<SandboxResponse<Post>> save(Post post) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<SandboxResponse<Integer>> delete(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SandboxResponse<List<Post>> saveSync(List<Post> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SandboxResponse<Post> saveSync(Post post) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addListener(Action1<Post> dataListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeListener(Action1<Post> dataListener) {
        throw new UnsupportedOperationException();
    }

}
