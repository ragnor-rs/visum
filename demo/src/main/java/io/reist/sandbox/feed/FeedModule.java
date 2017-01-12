/*
 * Copyright (C) 2017 Renat Sarymsakov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    public static final String BASE_URL = "https://visumfeed.herokuapp.com/";

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
    protected FeedService feedService(FeedServerApi feedApi, StorIOSQLite storIOSQLite) {
        return new CachedFeedService(new StorIoPostService(storIOSQLite), new RetrofitFeedService(feedApi));
    }
}
