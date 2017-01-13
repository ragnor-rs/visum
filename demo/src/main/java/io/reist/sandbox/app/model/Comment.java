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
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import io.reist.sandbox.feed.model.local.CommentTable;

/**
 * Created by 4xes on 18/11/16.
 */
@StorIOSQLiteType(table = CommentTable.NAME)
public class Comment {

    @SerializedName("id")
    @StorIOSQLiteColumn(name = CommentTable.Column.ID, key = true)
    public Long id;

    @StorIOSQLiteColumn(name = CommentTable.Column.EMAIL)
    @SerializedName("email")
    public String email;

    @SerializedName("post_id")
    @StorIOSQLiteColumn(name = CommentTable.Column.POST_ID)
    public Long post_id;

    @StorIOSQLiteColumn(name = CommentTable.Column.MESSAGE)
    @SerializedName("message")
    public String message;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}