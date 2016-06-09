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

package io.reist.sandbox.app.model;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import io.reist.sandbox.app.model.local.BaseTable;
import io.reist.sandbox.users.model.local.UserTable;

/**
 * Created by m039 on 11/12/15.
 */

@StorIOSQLiteType(table = UserTable.NAME)
public class User {

    @SerializedName("id")
    @StorIOSQLiteColumn(name = UserTable.Column.ID, key = true)
    public Long id;

    @SerializedName("revision")
    @StorIOSQLiteColumn(name = BaseTable.Column.REVISION)
    public int revision;

    @SerializedName("name")
    @StorIOSQLiteColumn(name = UserTable.Column.NAME)
    public String name;

    @SerializedName("login")
    @StorIOSQLiteColumn(name = UserTable.Column.LOGIN)
    public String login;

    public String getName() {
        return login; // WTF?
    }

    @Override
    public String toString() {
        return User.class.getSimpleName() + "{name = \"" + name + "\", id = " + id + "}";
    }

}
