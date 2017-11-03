package io.reist.sandbox.cryptocurrency.model;

import io.reist.sandbox.app.model.CachedService;
import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyItem;

public class CryptoCurrencyCachedService extends CachedService<CryptoCurrencyItem> implements CryptoCurrencyBaseService {

    public CryptoCurrencyCachedService(CryptoCurrencyBaseService local, CryptoCurrencyBaseService remote) {
        super(local, remote);
    }

}