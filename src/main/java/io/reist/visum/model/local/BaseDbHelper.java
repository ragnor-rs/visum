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
