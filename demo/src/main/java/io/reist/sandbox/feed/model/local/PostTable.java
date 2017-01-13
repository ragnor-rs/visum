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

import io.reist.sandbox.app.model.local.BaseTable;

public class PostTable extends BaseTable {
    public static final String NAME = "post";
    private static final String CREATE_TABLE =
        "create table " + NAME + "(" +
            Column.ID + " integer not null primary key, " +
            Column.TITLE + " text, " +
            Column.BODY + " text, " +
            Column.IMAGE + " text" +
        ")";

    @Override
    public String getCreateTableQuery() {
        return CREATE_TABLE;
    }

    @Override
    public String[] getUpgradeTableQueries(int oldVersion) {
        switch (oldVersion) {
            case 3:
                return new String[] {CREATE_TABLE};
            default:
                return null;
        }
    }

    public static final class Column {
        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String BODY = "body";
        public static final String IMAGE = "image";

        private Column() {
        }
    }
}