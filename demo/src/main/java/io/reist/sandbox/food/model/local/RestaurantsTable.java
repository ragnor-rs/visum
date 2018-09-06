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

package io.reist.sandbox.food.model.local;

import android.util.Log;

import io.reist.sandbox.app.model.local.BaseTable;

/**
 * Created by m039 on 11/12/15.
 */
public class RestaurantsTable extends BaseTable {

    public static final String NAME = "restaurants";

    public static final class Column extends BaseTable.Column {

        private Column() {
        }

        public static final String NAME = "name";
        public static final String RATING = "rating";
        public static final String LAT = "lat";
        public static final String LON = "lon";

    }

    private static final String CREATE_TABLE = "create table " + NAME + "(" +
            Column.ID + " text not null primary key, " +
            Column.LAT + " real," +
            Column.LON + " real, " +
            Column.NAME + " text, " +
            Column.RATING + " text" +
            ")";



    @Override
    public String[] getUpgradeTableQueries(int oldVersion) {
        return null;
    }

    @Override
    public String getCreateTableQuery() {
        Log.d("","");
        return CREATE_TABLE;
    }
}
