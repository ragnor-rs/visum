package io.reist.sandbox.feed.model.local;

import io.reist.sandbox.app.model.local.BaseTable;

public class PostTable extends BaseTable {
    public static final String NAME = "post";
    private static final String CREATE_TABLE =
        "create table " + NAME + "(" +
            Column.ID + " integer not null primary key, " +
            Column.TITLE + " text, " +
            Column.BODY + " text" +
        ")";

    @Override
    public String getCreateTableQuery() {
        return CREATE_TABLE;
    }

    @Override
    public String[] getUpgradeTableQueries(int oldVersion) {
        switch (oldVersion) {
            case 3:
                return new String[] {CREATE_TABLE};
            default:
                return null;
        }
    }

    public static final class Column {
        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String BODY = "body";

        private Column() {
        }
    }
}