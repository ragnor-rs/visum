package io.reist.sandbox.feed.model.local;

import io.reist.sandbox.app.model.local.BaseTable;

public class CommentTable extends BaseTable {
    public static final String NAME = "comment";
    private static final String CREATE_TABLE =
        "create table " + NAME + "(" +
            Column.ID + " integer not null primary key, " +
            Column.MESSAGE + " text, " +
            "FOREIGN KEY (" + Column.POST_ID + ") " +
            "REFERENCES " + PostTable.NAME + "(" + PostTable.Column.ID + ")" +
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
        public static final String POST_ID = "post_id";
        public static final String MESSAGE = "message";

        private Column() {
        }
    }
}