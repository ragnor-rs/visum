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

import io.reist.sandbox.app.model.local.BaseTable;
import io.reist.sandbox.users.model.local.UserTable;

/**
 * Created by Reist on 10/16/15.
 */
public class RepoTable extends BaseTable {

    public static final String NAME = "repo";

    public static final class Column extends BaseTable.Column {

        private Column() {}

        public static final String NAME = "name";
        public static final String URL = "url";
        public static final String LIKE_COUNT = "like_count";
        public static final String USER_ID = "user_id";
        public static final String LIKED_BY_ME = "liked_by_me";
    }

    private static final String CREATE_TABLE = "create table " + NAME + "(" +
            Column.ID + " integer not null primary key, " +
            Column.REVISION + " integer," +
            Column.NAME + " text, " +
            Column.URL + " text, " +
            Column.LIKE_COUNT + " integer, " +
            Column.USER_ID + " integer not null, " +
            Column.LIKED_BY_ME + " integer, " +
            "FOREIGN KEY (" + Column.USER_ID + ") " +
            "REFERENCES " + UserTable.NAME + "(" + UserTable.Column.ID + ")" +
            ")";

    @Override
    public String getCreateTableQuery() {
        return CREATE_TABLE;
    }

    @Override
    public String[] getUpgradeTableQueries(int oldVersion) {
        switch (oldVersion) {

            case 2:
                return new String[] {
                        "drop table " + NAME,
                        CREATE_TABLE
                };

            case 1:
                return new String[] {

                        "alter table " +
                                NAME + " " +
                                "add column author text " +
                                "default " +
                                "\"JakeWharton\""

                };

            default:
                return new String[0];

        }
    }

}