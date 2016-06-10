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

package io.reist.sandbox.repos.model.local;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

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
    public Repo mapFromCursor(@NonNull Cursor cursor) {
        Repo repo = defaultGetResolver.mapFromCursor(cursor);

        long userId = cursor.getLong(cursor.getColumnIndex(RepoTable.Column.USER_ID));

        final StorIOSQLite storIOSQLite = storIOSQLiteFromPerformGet.get();

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
        return storIOSQLite.internal().rawQuery(rawQuery);
    }

    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull Query query) {
        storIOSQLiteFromPerformGet.set(storIOSQLite);
        return storIOSQLite.internal().query(query);
    }

}
