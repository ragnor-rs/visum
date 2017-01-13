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

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;

import java.util.HashSet;
import java.util.Set;

import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.PostStorIOSQLitePutResolver;

/**
 * Created by 4xes on 18/11/16.
 */
public class PostPutResolver extends PutResolver<Post> {

    PutResolver<Post> defaultPutResolver = new PostStorIOSQLitePutResolver();

    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull Post post) {
        boolean hasComments = post.comments != null && !post.comments.isEmpty();


        if (hasComments) {
            storIOSQLite
                    .put()
                    .objects(post.comments)
                    .prepare()
                    .executeAsBlocking();
        }

        defaultPutResolver.performPut(storIOSQLite, post);


        final Set<String> affectedTables = new HashSet<>(2);
        affectedTables.add(PostTable.NAME);
        if (hasComments) {
            affectedTables.add(CommentTable.NAME);
        }

        int affectedObjects = 1 + (hasComments ? post.comments.size() : 0);

        return PutResult.newUpdateResult(affectedObjects, affectedTables);
    }
}
