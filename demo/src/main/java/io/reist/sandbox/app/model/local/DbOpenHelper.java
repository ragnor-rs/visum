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

package io.reist.sandbox.app.model.local;

import android.content.Context;
import android.support.annotation.NonNull;

import io.reist.sandbox.feed.model.local.CommentTable;
import io.reist.sandbox.feed.model.local.PostTable;
import io.reist.sandbox.repos.model.local.RepoTable;
import io.reist.sandbox.users.model.local.UserTable;

public class DbOpenHelper extends BaseDbHelper {

    private static final String DATABASE_NAME = "sandbox";
    private static final int DATABASE_VERSION = 4;

    public DbOpenHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
        addTable(RepoTable.class);
        addTable(UserTable.class);
        addTable(PostTable.class);
        addTable(CommentTable.class);
    }

}