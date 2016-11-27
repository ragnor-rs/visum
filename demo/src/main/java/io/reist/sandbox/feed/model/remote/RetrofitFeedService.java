package io.reist.sandbox.feed.model.remote;

import java.util.List;

import javax.inject.Inject;

import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.feed.model.FeedService;
import rx.Observable;

/**
 * Created by 4xes on 4/11/16.
 */
public class RetrofitFeedService implements FeedService {

    private final FeedServerApi feedServerApi;

    @Inject
    public RetrofitFeedService(FeedServerApi api) {
        this.feedServerApi = api;
    }

    @Override
    public Observable<SandboxResponse<List<Post>>> list() {
        return feedServerApi.posts().map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<Post>> byId(Long id) {
        return feedServerApi.post(id).map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<List<Post>>> save(List<Post> list) {
        throw new UnsupportedOperationException("you cannot save make api calls");
    }

    @Override
    public Observable<SandboxResponse<Post>> save(Post post) {
        throw new UnsupportedOperationException("you cannot save make api calls");
    }

    @Override
    public Observable<SandboxResponse<Integer>> delete(Long id) {
        throw new UnsupportedOperationException("you cannot delete by id");
    }

    @Override
    public SandboxResponse<List<Post>> saveSync(List<Post> list) {
        throw new UnsupportedOperationException("you cannot save make api calls synchronously");
    }

    @Override
    public SandboxResponse<Post> saveSync(Post post) {
        throw new UnsupportedOperationException("you cannot save make api calls synchronously");
    }
}
