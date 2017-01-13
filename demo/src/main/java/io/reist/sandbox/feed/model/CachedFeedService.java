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

package io.reist.sandbox.feed.model;

import io.reist.sandbox.app.model.CachedService;
import io.reist.sandbox.app.model.Post;

/**
 * Created by 4xes on 7/11/16.
 */
public class CachedFeedService extends CachedService<Post> implements FeedService {

    protected final FeedService local;
    protected final FeedService remote;

    public CachedFeedService(FeedService local, FeedService remote) {
        super(local, remote);

        this.local = local;
        this.remote = remote;
    }

}
