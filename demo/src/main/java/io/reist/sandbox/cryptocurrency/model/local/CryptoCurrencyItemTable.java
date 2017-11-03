package io.reist.sandbox.cryptocurrency.model.local;

import io.reist.sandbox.app.model.local.BaseTable;

public class CryptoCurrencyItemTable extends BaseTable {

    public static final String NAME = "CryptoCurrencyItemTable";

    private static final String CREATE_TABLE =
        "create table " + NAME + " (" +
            Column.LABEL + " text primary key," +
            Column.PRICE + " text, " +
            Column.IMAGE + " text" +
        ")";

    @Override
    public String getCreateTableQuery() {
        return CREATE_TABLE;
    }

    @Override
    public String[] getUpgradeTableQueries(int oldVersion) {
                return null;
    }

    public static final class Column {
        public static final String LABEL = "label";
        public static final String PRICE = "price";
        public static final String IMAGE = "image";
    }

}