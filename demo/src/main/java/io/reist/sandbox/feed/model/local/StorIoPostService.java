package io.reist.sandbox.feed.model.local;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.local.StorIoService;
import io.reist.sandbox.feed.model.FeedService;
import rx.Observable;

/**
 * Created by 4xes on 8/11/16.
 */
public class StorIoPostService extends StorIoService<Post> implements FeedService {

    public StorIoPostService(StorIOSQLite storIOSQLite) {
        super(storIOSQLite);
    }

    @Override
    public Observable<SandboxResponse<List<Post>>> list() {
        return preparedGetBuilder(Post.class)
                .withQuery(Query
                        .builder()
                        .table(PostTable.NAME)
                        .orderBy(PostTable.Column.ID)
                        .build())
                .prepare()
                .createObservable()
                .map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<Post>> byId(Long id) {
        return unique(Post.class, PostTable.NAME, id).map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<Integer>> delete(Long id) {
        throw new IllegalStateException("Unsupported");
    }
}