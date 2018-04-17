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

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio2.sqlite.StorIOSQLite;
import com.pushtorefresh.storio2.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio2.sqlite.queries.Query;
import com.pushtorefresh.storio2.sqlite.queries.RawQuery;

import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.RepoStorIOSQLiteGetResolver;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.users.model.local.UserTable;

/**
 * Created by m039 on 11/17/15.
 */
public class RepoGetResolver extends GetResolver<Repo> {

    // This is hack, look at
    // https://github.com/pushtorefresh/storio/blob/master/storio-sample-app/src/main/java/com/pushtorefresh/storio/sample/db/resolvers/UserWithTweetsGetResolver.java
    private final ThreadLocal<StorIOSQLite> storIOSQLiteFromPerformGet = new ThreadLocal<>();

    private final GetResolver<Repo> defaultGetResolver = new RepoStorIOSQLiteGetResolver();

    @NonNull
    @Override
    public Repo mapFromCursor(@NonNull StorIOSQLite storIOSQLite, @NonNull Cursor cursor) {
        Repo repo = defaultGetResolver.mapFromCursor(storIOSQLite, cursor);

        long userId = cursor.getLong(cursor.getColumnIndex(RepoTable.Column.USER_ID));

        User owner = storIOSQLite
                .get()
                .listOfObjects(User.class)
                .withQuery(Query
                        .builder()
                        .table(UserTable.NAME)
                        .where(UserTable.Column.ID + " = ?")
                        .whereArgs(userId)
                        .limit(1)
                        .build())
                .prepare()
                .executeAsBlocking()
                .get(0);

        repo.owner = owner;

        return repo;
    }

    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull RawQuery rawQuery) {
        storIOSQLiteFromPerformGet.set(storIOSQLite);
        return storIOSQLite.lowLevel().rawQuery(rawQuery);
    }

    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull Query query) {
        storIOSQLiteFromPerformGet.set(storIOSQLite);
        return storIOSQLite.lowLevel().query(query);
    }

}
