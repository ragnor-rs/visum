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

package io.reist.sandbox.app;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reist.sandbox.app.model.Comment;
import io.reist.sandbox.app.model.CommentStorIOSQLiteDeleteResolver;
import io.reist.sandbox.app.model.CommentStorIOSQLiteGetResolver;
import io.reist.sandbox.app.model.CommentStorIOSQLitePutResolver;
import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.PostStorIOSQLiteDeleteResolver;
import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.RepoStorIOSQLiteDeleteResolver;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.app.model.UserStorIOSQLiteDeleteResolver;
import io.reist.sandbox.app.model.UserStorIOSQLiteGetResolver;
import io.reist.sandbox.app.model.UserStorIOSQLitePutResolver;
import io.reist.sandbox.app.model.local.DbOpenHelper;
import io.reist.sandbox.app.model.remote.NestedFieldNameAdapter;
import io.reist.sandbox.app.model.remote.SandboxApi;
import io.reist.sandbox.cryptocurrency.CryptoCurrencyModule;
import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyItem;
import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyItemStorIOSQLiteDeleteResolver;
import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyItemStorIOSQLiteGetResolver;
import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyItemStorIOSQLitePutResolver;
import io.reist.sandbox.feed.FeedModule;
import io.reist.sandbox.feed.model.local.PostGetResolver;
import io.reist.sandbox.feed.model.local.PostPutResolver;
import io.reist.sandbox.repos.ReposModule;
import io.reist.sandbox.repos.model.local.RepoGetResolver;
import io.reist.sandbox.repos.model.local.RepoPutResolver;
import io.reist.sandbox.result.ResultModule;
import io.reist.sandbox.time.TimeModule;
import io.reist.sandbox.users.UsersModule;
import io.reist.sandbox.weather.WeatherModule;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = {
        UsersModule.class,
        ReposModule.class,
        TimeModule.class,
        ResultModule.class,
        WeatherModule.class,
        FeedModule.class,
        CryptoCurrencyModule.class
})
public class SandboxModule {

    public static final String REMOTE_SERVICE = "remote";
    public static final String LOCAL_SERVICE = "local";

    public static final String GIT_HUB_BASE_URL = "https://safe-reaches-4393.herokuapp.com";

    private static final String TAG = SandboxModule.class.getName();

    private final Context context;

    public SandboxModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    StorIOSQLite storIoSqLite() {

        DbOpenHelper dbOpenHelper = new DbOpenHelper(context);

        return DefaultStorIOSQLite
                .builder()
                .sqliteOpenHelper(dbOpenHelper)
                .addTypeMapping(
                        Repo.class,
                        SQLiteTypeMapping.<Repo>builder()
                                .putResolver(new RepoPutResolver())
                                .getResolver(new RepoGetResolver())
                                .deleteResolver(new RepoStorIOSQLiteDeleteResolver())
                                .build()
                )
                .addTypeMapping(
                        User.class,
                        SQLiteTypeMapping.<User>builder()
                                .putResolver(new UserStorIOSQLitePutResolver())
                                .getResolver(new UserStorIOSQLiteGetResolver())
                                .deleteResolver(new UserStorIOSQLiteDeleteResolver())
                                .build()
                )
                .addTypeMapping(
                        Post.class,
                        SQLiteTypeMapping.<Post>builder()
                                .putResolver(new PostPutResolver())
                                .getResolver(new PostGetResolver())
                                .deleteResolver(new PostStorIOSQLiteDeleteResolver())
                                .build()
                )
                .addTypeMapping(
                        Comment.class,
                        SQLiteTypeMapping.<Comment>builder()
                                .putResolver(new CommentStorIOSQLitePutResolver())
                                .getResolver(new CommentStorIOSQLiteGetResolver())
                                .deleteResolver(new CommentStorIOSQLiteDeleteResolver())
                                .build()
                )
                .addTypeMapping(
                        CryptoCurrencyItem.class,
                        SQLiteTypeMapping.<CryptoCurrencyItem>builder()
                                .putResolver(new CryptoCurrencyItemStorIOSQLitePutResolver())
                                .getResolver(new CryptoCurrencyItemStorIOSQLiteGetResolver())
                                .deleteResolver(new CryptoCurrencyItemStorIOSQLiteDeleteResolver())
                                .build()
                )
                .build();

    }

    @Provides @Singleton
    SandboxApi gitHubApi() {

        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Object.class, new NestedFieldNameAdapter())
                .create();

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {

                    Request request = chain.request();
                    HttpUrl httpUrl = request
                            .url()
                            .newBuilder()
                            .addQueryParameter("user_id", "1")
                            .build();
                    request = request.newBuilder().url(httpUrl).build();

                    Log.i(TAG, request.toString());

                    return chain.proceed(request);

                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(GIT_HUB_BASE_URL)
                .client(httpClient)
                .build();

        return retrofit.create(SandboxApi.class);

    }

    @Provides @Singleton
    SandboxApplication sandboxApplication() {
        return (SandboxApplication) context.getApplicationContext();
    }

}
