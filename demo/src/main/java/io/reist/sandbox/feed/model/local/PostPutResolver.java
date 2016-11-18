package io.reist.sandbox.feed.model.local;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reist.sandbox.app.model.Post;

/**
 * Created by 4xes on 18/11/16.
 */
public class PostPutResolver extends PutResolver<Post>{

    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull Post post) {
        final List<Object> objectsToPut = new ArrayList<>(1 + post.comments.size());

        objectsToPut.add(post);
        objectsToPut.addAll(post.comments);

        storIOSQLite
                .put()
                .objects(objectsToPut)
                .prepare()
                .executeAsBlocking();

        final Set<String> affectedTables = new HashSet<>(2);
        affectedTables.add(PostTable.NAME);
        affectedTables.add(CommentTable.NAME);

        return PutResult.newUpdateResult(objectsToPut.size(), affectedTables);
    }
}
