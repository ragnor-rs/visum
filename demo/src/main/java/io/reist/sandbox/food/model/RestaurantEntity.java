package io.reist.sandbox.food.model;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import io.reist.sandbox.app.model.User;
import io.reist.sandbox.app.model.local.BaseTable;
import io.reist.sandbox.food.model.local.RestaurantsTable;
import io.reist.sandbox.users.model.local.UserTable;

/**
 * Created by Fedorov-DA on 05.03.2018.
 */


@StorIOSQLiteType(table = RestaurantsTable.NAME)
public class RestaurantEntity {

    @SerializedName("id")
    @StorIOSQLiteColumn(name = RestaurantsTable.Column.ID, key = true)
    public String id;

    @SerializedName("name")
    @StorIOSQLiteColumn(name = RestaurantsTable.Column.NAME)
    public String name;

    @SerializedName("lat")
    @StorIOSQLiteColumn(name = RestaurantsTable.Column.LAT)
    public double lat;

    @SerializedName("lon")
    @StorIOSQLiteColumn(name = RestaurantsTable.Column.LON)
    public double lon;

    @SerializedName("rating")
    @StorIOSQLiteColumn(name = RestaurantsTable.Column.RATING)
    public String rating;



    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{name = \"" + name + "\", id = " + id + "}";
    }

}
