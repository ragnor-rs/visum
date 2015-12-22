/*
 * Copyright (c) 2015  Zvooq LTD.
 * Authors: Renat Sarymsakov, Dmitriy Mozgin, Denis Volyntsev.
 *
 * This file is part of Visum.
 *
 * Visum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Visum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Visum.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.reist.visum.model.local;

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
