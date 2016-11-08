package io.reist.sandbox.feed;


import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reist.sandbox.feed.model.CachedFeedService;
import io.reist.sandbox.feed.model.FeedService;
import io.reist.sandbox.feed.model.local.StorIoPostService;
import io.reist.sandbox.feed.model.remote.FeedServerApi;
import io.reist.sandbox.feed.model.remote.RetrofitFeedService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class FeedModule {

    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    @Provides
    @Singleton
    FeedServerApi feedApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(FeedServerApi.class);
    }

    @Provides
    @Singleton
    protected FeedService repoService(FeedServerApi feedApi, StorIOSQLite storIOSQLite) {
        return new CachedFeedService(new StorIoPostService(storIOSQLite), new RetrofitFeedService(feedApi));
    }
}
