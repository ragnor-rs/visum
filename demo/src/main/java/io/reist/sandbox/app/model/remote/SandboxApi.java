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
