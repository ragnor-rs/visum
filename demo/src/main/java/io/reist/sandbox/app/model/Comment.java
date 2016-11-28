package io.reist.sandbox.app.model;


import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import io.reist.sandbox.feed.model.local.CommentTable;

/**
 * Created by 4xes on 18/11/16.
 */
@StorIOSQLiteType(table = CommentTable.NAME)
public class Comment {

    @SerializedName("id")
    @StorIOSQLiteColumn(name = CommentTable.Column.ID, key = true)
    public Long id;

    @StorIOSQLiteColumn(name = CommentTable.Column.EMAIL)
    @SerializedName("email")
    public String email;

    @SerializedName("post_id")
    @StorIOSQLiteColumn(name = CommentTable.Column.POST_ID)
    public Long post_id;

    @StorIOSQLiteColumn(name = CommentTable.Column.MESSAGE)
    @SerializedName("message")
    public String message;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}