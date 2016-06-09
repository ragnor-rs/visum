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

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;

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
