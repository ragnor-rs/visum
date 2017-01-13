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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reist on 10/23/15.
 */
public abstract class BaseDbHelper extends SQLiteOpenHelper {

    private final List<BaseTable> tables = new ArrayList<>();

    public BaseDbHelper(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
    }

    protected final void addTable(Class<? extends BaseTable> tableClass) {
        try {
            tables.add(tableClass.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final void onCreate(@NonNull SQLiteDatabase db) {
        for (BaseTable table : tables) {
            db.execSQL(table.getCreateTableQuery());
        }
    }

    @Override
    public final void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        for (BaseTable table : tables) {
            String[] upgradeTableQueries = table.getUpgradeTableQueries(oldVersion);
            if (upgradeTableQueries != null) {
                for (String query : upgradeTableQueries) {
                    db.execSQL(query);
                }
            }
        }
    }

}
