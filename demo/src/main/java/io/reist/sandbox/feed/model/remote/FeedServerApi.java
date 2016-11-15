package io.reist.sandbox.feed.model.remote;

import java.util.List;

import io.reist.sandbox.app.model.Post;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 4xes on 4/11/16.
 */
public interface FeedServerApi {

    @GET("/json/feed.json")
    Observable<List<Post>> posts();

    @GET("/json/{postId}.json")
    Observable<Post> post(@Path("postId") long postId);
}