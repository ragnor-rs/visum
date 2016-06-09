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
import io.reist.sandbox.repos.model.local.RepoTable;

@StorIOSQLiteType(table = RepoTable.NAME)
public class Repo {

    @SerializedName("id")
    @StorIOSQLiteColumn(name = RepoTable.Column.ID, key = true)
    public Long id;

    @SerializedName("revision")
    @StorIOSQLiteColumn(name = BaseTable.Column.REVISION)
    public int revision;

    @SerializedName("name")
    @StorIOSQLiteColumn(name = RepoTable.Column.NAME)
    public String name;

    @SerializedName("html_url")
    @StorIOSQLiteColumn(name = RepoTable.Column.URL)
    public String url;

    @SerializedName("like_count")
    @StorIOSQLiteColumn(name = RepoTable.Column.LIKE_COUNT)
    public int likeCount;

    @SerializedName("liked_by_me")
    @StorIOSQLiteColumn(name = RepoTable.Column.LIKED_BY_ME)
    public boolean likedByMe;

    @SerializedName("owner")
    public User owner;

    public boolean isLiked() {
        return likedByMe;
    }

    @Override
    public String toString() {
        return name;
    }
}
