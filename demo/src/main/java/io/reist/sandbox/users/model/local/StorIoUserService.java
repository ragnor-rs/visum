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
                .map(SandboxResponse::new);
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
