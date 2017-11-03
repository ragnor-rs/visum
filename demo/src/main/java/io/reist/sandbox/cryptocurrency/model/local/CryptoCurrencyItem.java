package io.reist.sandbox.cryptocurrency.model.local;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by Sergey on 02/11/2017.
 */

@StorIOSQLiteType(table = CryptoCurrencyItemTable.NAME)
public class CryptoCurrencyItem {

    @StorIOSQLiteColumn(name = CryptoCurrencyItemTable.Column.LABEL, key = true)
    public String label;

    @StorIOSQLiteColumn(name = CryptoCurrencyItemTable.Column.PRICE)
    public String price;

    @StorIOSQLiteColumn(name = CryptoCurrencyItemTable.Column.IMAGE)
    public String image;

    public CryptoCurrencyItem() {
    }

    public CryptoCurrencyItem(String label, String price, String image) {
        this.label = label;
        this.price = price;
        this.image = image;
    }

}