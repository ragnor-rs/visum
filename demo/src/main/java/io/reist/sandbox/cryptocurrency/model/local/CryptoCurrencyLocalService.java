package io.reist.sandbox.cryptocurrency.model.local;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.local.StorIoService;
import io.reist.sandbox.cryptocurrency.model.CryptoCurrencyBaseService;
import rx.Observable;

public class CryptoCurrencyLocalService extends StorIoService<CryptoCurrencyItem> implements CryptoCurrencyBaseService {

    public CryptoCurrencyLocalService(StorIOSQLite storIoSqLite) {
        super(storIoSqLite);
    }

    @Override
    public Observable<SandboxResponse<List<CryptoCurrencyItem>>> list() {
        return preparedGetBuilder(CryptoCurrencyItem.class)
            .withQuery(Query
                .builder()
                .table(CryptoCurrencyItemTable.NAME)
                .build())
            .prepare()
            .createObservable()
            .map(SandboxResponse::new);
    }

    @Override
    public Observable<SandboxResponse<CryptoCurrencyItem>> byId(Long id) {
        return null;
    }

    @Override
    public Observable<SandboxResponse<Integer>> delete(Long id) {
        return null;
    }

}