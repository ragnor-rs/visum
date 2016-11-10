package io.reist.sandbox.feed.model.local;


import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

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

    @Override
    public String toString() {
        return Post.class.getSimpleName() + "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}