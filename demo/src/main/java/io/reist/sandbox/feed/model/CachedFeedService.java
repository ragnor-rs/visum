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
