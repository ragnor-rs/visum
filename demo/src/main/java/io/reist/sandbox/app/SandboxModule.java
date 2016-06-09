/*
 * Copyright (c) 2015  Zvooq LTD.
 * Authors: Renat Sarymsakov, Dmitriy Mozgin, Denis Volyntsev.
 *
 * This file is part of MVP-Sandbox.
 *
 * MVP-Sandbox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MVP-Sandbox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MVP-Sandbox.  If not, see <http://www.gnu.org/licenses/>.
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
import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.RepoStorIOSQLiteDeleteResolver;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.app.model.UserStorIOSQLiteDeleteResolver;
import io.reist.sandbox.app.model.UserStorIOSQLiteGetResolver;
import io.reist.sandbox.app.model.UserStorIOSQLitePutResolver;
import io.reist.sandbox.app.model.local.DbOpenHelper;
import io.reist.sandbox.app.model.remote.NestedFieldNameAdapter;
import io.reist.sandbox.app.model.remote.SandboxApi;
import io.reist.sandbox.repos.ReposModule;
import io.reist.sandbox.repos.model.local.RepoGetResolver;
import io.reist.sandbox.repos.model.local.RepoPutResolver;
import io.reist.sandbox.result.ResultModule;
import io.reist.sandbox.time.TimeModule;
import io.reist.sandbox.users.UsersModule;
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
        ResultModule.class
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
