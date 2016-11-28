package io.reist.sandbox.feed.model.local;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import io.reist.sandbox.app.model.Comment;
import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.PostStorIOSQLiteGetResolver;

public class PostGetResolver extends GetResolver<Post> {

    // This is hack, look at
    // https://github.com/pushtorefresh/storio/blob/master/storio-sample-app/src/main/java/com/pushtorefresh/storio/sample/db/resolvers/UserWithTweetsGetResolver.java
    private final ThreadLocal<StorIOSQLite> storIOSQLiteFromPerformGet = new ThreadLocal<>();

    private final GetResolver<Post> postGetResolver = new PostStorIOSQLiteGetResolver();

    @NonNull
    @Override
    public Post mapFromCursor(@NonNull Cursor cursor) {
        final StorIOSQLite storIOSQLite = storIOSQLiteFromPerformGet.get();
        final Post post = postGetResolver.mapFromCursor(cursor);

        post.comments = storIOSQLite
                .get()
                .listOfObjects(Comment.class)
                .withQuery(Query
                        .builder()
                        .table(CommentTable.NAME)
                        .where(CommentTable.Column.POST_ID + " = ?")
                        .whereArgs(post.id)
                        .build())
                .prepare()
                .executeAsBlocking();
        return post;
    }

    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull RawQuery rawQuery) {
        storIOSQLiteFromPerformGet.set(storIOSQLite);
        return storIOSQLite.internal().rawQuery(rawQuery);
    }

    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull Query query) {
        storIOSQLiteFromPerformGet.set(storIOSQLite);
        return storIOSQLite.internal().query(query);
    }
}
