package io.reist.visum.model.local;

/**
 * Created by Reist on 10/23/15.
 */
public abstract class BaseTable {

    public interface Column {
        String ID = "_id";
        String REVISION = "revision";
    }

    public abstract String getCreateTableQuery();

    public abstract String[] getUpgradeTableQueries(int oldVersion);

}
