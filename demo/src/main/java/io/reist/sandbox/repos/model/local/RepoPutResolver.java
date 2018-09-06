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

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio2.sqlite.StorIOSQLite;
import com.pushtorefresh.storio2.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio2.sqlite.operations.put.PutResult;

import java.util.HashSet;
import java.util.Set;

import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.RepoStorIOSQLitePutResolver;
import io.reist.sandbox.users.model.local.UserTable;

/**
 * Created by m039 on 11/17/15.
 */
public class RepoPutResolver extends PutResolver<Repo> {

    PutResolver<Repo> defaultPutResolver = new RepoStorIOSQLitePutResolver() {

        @NonNull
        @Override
        public ContentValues mapToContentValues(@NonNull Repo object) {
            ContentValues contentValues = super.mapToContentValues(object);

            contentValues.put(RepoTable.Column.USER_ID, object.owner.id);

            return contentValues;
        }

    };

    @NonNull
    @Override
    public PutResult performPut(StorIOSQLite storIOSQLite, Repo object) {
        if (object.owner == null || object.owner.id == null) {
            return PutResult.newUpdateResult(0, new HashSet<>());
        }

        storIOSQLite
                .put()
                .object(object.owner)
                .prepare()
                .executeAsBlocking();

        PutResult repoResult = defaultPutResolver.performPut(storIOSQLite, object);

        Set<String> affectedTables = new HashSet<>(2);

        affectedTables.add(UserTable.NAME);
        affectedTables.add(RepoTable.NAME);

        if (repoResult.wasInserted()) {
            return PutResult.newInsertResult(repoResult.insertedId(), affectedTables);
        } else {
            return PutResult.newUpdateResult(1, affectedTables);
        }
    }

}
