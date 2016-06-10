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

package io.reist.sandbox.users.model.local;

import io.reist.sandbox.app.model.local.BaseTable;

/**
 * Created by m039 on 11/12/15.
 */
public class UserTable extends BaseTable {

    public static final String NAME = "user";

    public static final class Column extends BaseTable.Column {

        private Column() {}

        public static final String NAME = "name";
        public static final String LOGIN = "login";
    }

    private static final String CREATE_TABLE = "create table " + NAME + "(" +
            Column.ID + " integer not null primary key, " +
            Column.REVISION + " integer," +
            Column.NAME + " text, " +
            Column.LOGIN + " text" +
            ")";

    @Override
    public String[] getUpgradeTableQueries(int oldVersion) {
        switch (oldVersion) {
            case 2:
                return new String[]{CREATE_TABLE};
            default:
                return null;
        }
    }

    @Override
    public String getCreateTableQuery() {
        return CREATE_TABLE;
    }
}
