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

package io.reist.sandbox.app.model.local;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio2.sqlite.StorIOSQLite;
import com.pushtorefresh.storio2.sqlite.operations.get.PreparedGetListOfObjects;
import com.pushtorefresh.storio2.sqlite.operations.put.PreparedPut;
import com.pushtorefresh.storio2.sqlite.queries.Query;

import java.util.List;

import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.SandboxService;
import rx.Observable;

/**
 * Created by Reist on 10/23/15.
 */
public abstract class StorIoService<T> implements SandboxService<T> {

    protected final StorIOSQLite storIoSqLite;

    public StorIoService(StorIOSQLite storIoSqLite) {
        this.storIoSqLite = storIoSqLite;
    }

    @NonNull
    protected PreparedGetListOfObjects.Builder<T> preparedGetBuilder(Class<T> entityClass) {
        return storIoSqLite.get().listOfObjects(entityClass);
    }

    @NonNull
    protected PreparedPut.Builder preparedPutBuilder() {
        return storIoSqLite.put();
    }

    @NonNull
    protected final Observable<T> unique(Class<T> entityClass, String tableName, Long id) {
        return preparedGetBuilder(entityClass)
                .withQuery(
                        Query.builder()
                                .table(tableName)
                                .where(BaseTable.Column.ID + " = ?")
                                .whereArgs(id)
                                .build()
                )
                .prepare()
                .asRxObservable()
                .map(list -> list == null || list.isEmpty() ?
                        null :
                        list.get(0));
    }

    @Override
    public final SandboxResponse<List<T>> saveSync(List<T> list) {

        preparedPutBuilder()
                .objects(list)
                .prepare()
                .executeAsBlocking();

        return new SandboxResponse<>(list);
    }

    @Override
    public final SandboxResponse<T> saveSync(T t) {

        preparedPutBuilder()
                .object(t)
                .prepare()
                .executeAsBlocking();

        return new SandboxResponse<>(t);
    }

    @Override
    public Observable<SandboxResponse<List<T>>> save(List<T> list) {
        return Observable.just(saveSync(list));
    }

    @Override
    public Observable<SandboxResponse<T>> save(T t) {
        return Observable.just(saveSync(t));
    }

}
