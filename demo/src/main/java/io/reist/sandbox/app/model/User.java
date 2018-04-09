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

package io.reist.sandbox.app.model;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio2.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio2.sqlite.annotations.StorIOSQLiteType;

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
