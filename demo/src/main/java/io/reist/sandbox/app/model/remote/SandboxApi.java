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

package io.reist.sandbox.app.model.remote;

import java.util.List;

import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.User;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Reist on 10/14/15.
 */
public interface SandboxApi {

    @GET("/repos")
    Observable<SandboxResponse<List<Repo>>> listRepos();

    @GET("/repos/{id}")
    Observable<SandboxResponse<Repo>> repoById(@Path("id") Long id);

    @POST("/repos")
    Observable<SandboxResponse<Repo>> save(@Body Repo repo);

    @DELETE("/repos/{id}")
    Observable<SandboxResponse<Integer>> deleteRepo(@Path("id") Long id);

    @POST("/repos/{id}/like")
    Observable<SandboxResponse<Repo>> like(@Path("id") Long repoId);

    @POST("/repos/{id}/unlike")
    Observable<SandboxResponse<Repo>> unlike(@Path("id") Long repoId);

    @GET("/users")
    Observable<SandboxResponse<List<User>>> listUsers();

    @GET("/users/{id}/repos")
    Observable<SandboxResponse<List<Repo>>> reposByUserId(@Path("id") Long userId);

}
