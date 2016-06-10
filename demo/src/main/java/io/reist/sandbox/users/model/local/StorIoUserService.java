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

package io.reist.sandbox.users.model.local;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.app.model.local.StorIoService;
import io.reist.sandbox.users.model.UserService;
import rx.Observable;

/**
 * Created by m039 on 11/12/15.
 */
public class StorIoUserService extends StorIoService<User> implements UserService {


    public StorIoUserService(StorIOSQLite storIoSqLite) {
        super(storIoSqLite);
    }

    @Override
    public Observable<SandboxResponse<User>> byId(Long id) {
        return unique(User.class, UserTable.NAME, id)
                .map(SandboxResponse<User>::new);
    }

    @Override
    public Observable<SandboxResponse<Integer>> delete(Long id) {
        throw new IllegalStateException("Unsupported");
    }

    @Override
    public Observable<SandboxResponse<List<User>>> list() {
        return preparedGetBuilder(User.class)
                .withQuery(Query
                        .builder()
                        .table(UserTable.NAME)
                        .orderBy(UserTable.Column.ID)
                        .build())
                .prepare()
                .createObservable()
                .map(SandboxResponse::new);
    }

}
