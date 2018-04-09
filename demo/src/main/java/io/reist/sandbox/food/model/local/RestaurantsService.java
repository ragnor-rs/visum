package io.reist.sandbox.food.model.local;

import com.pushtorefresh.storio2.sqlite.StorIOSQLite;
import com.pushtorefresh.storio2.sqlite.queries.Query;

import java.util.List;

import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.SandboxService;
import io.reist.sandbox.app.model.local.StorIoService;
import io.reist.sandbox.food.model.entity.RestaurantEntity;
import rx.Observable;

/**
 * Created by Fedorov-DA on 05.03.2018.
 */

public class RestaurantsService extends StorIoService<RestaurantEntity> implements SandboxService<RestaurantEntity> {

    public RestaurantsService(StorIOSQLite storIoSqLite) {
        super(storIoSqLite);
    }

    @Override
    public Observable<SandboxResponse<List<RestaurantEntity>>> save(List<RestaurantEntity> list) {
        return Observable.just(saveSync(list));
    }

    @Override
    public Observable<SandboxResponse<List<RestaurantEntity>>> list() {
        return preparedGetBuilder(RestaurantEntity.class)
                .withQuery(Query
                        .builder()
                        .table(RestaurantsTable.NAME)
                        .build())
                .prepare()
                .asRxObservable()
                .map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<RestaurantEntity>> byId(Long id) {
        return unique(RestaurantEntity.class, RestaurantsTable.NAME, id)
                .map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<Integer>> delete(Long id) {
        return null;
    }
}
