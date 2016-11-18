package io.reist.sandbox.app.model;


import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import io.reist.sandbox.feed.model.local.PostTable;

/**
 * Created by 4xes on 8/11/16.
 */
@StorIOSQLiteType(table = PostTable.NAME)
public class Post {

    @SerializedName("id")
    @StorIOSQLiteColumn(name = PostTable.Column.ID, key = true)
    public Long id;

    @StorIOSQLiteColumn(name = PostTable.Column.TITLE)
    @SerializedName("title")
    public String title;

    @SerializedName("body")
    @StorIOSQLiteColumn(name = PostTable.Column.BODY)
    public String body;

    @SerializedName("image")
    @StorIOSQLiteColumn(name = PostTable.Column.IMAGE)
    public String image;

    @Override
    public String toString() {
        return Post.class.getSimpleName() + "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}