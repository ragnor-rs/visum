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
