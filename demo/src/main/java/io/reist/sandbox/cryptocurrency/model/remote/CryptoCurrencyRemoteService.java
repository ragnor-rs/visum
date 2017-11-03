package io.reist.sandbox.cryptocurrency.model.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.cryptocurrency.model.CryptoCurrencyBaseService;
import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyItem;
import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyList;
import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyPrice;
import rx.Observable;

/**
 * Created by Sergey on 03/11/2017.
 */

public final class CryptoCurrencyRemoteService implements CryptoCurrencyBaseService {

    private CryptoCurrencyServerListAPI api_list;
    private CryptoCurrencyServerPriceAPI api_price;

    private CryptoCurrencyList items_list;
    private Map<String, CryptoCurrencyPrice> items_price;

    public CryptoCurrencyRemoteService(CryptoCurrencyServerListAPI api_list, CryptoCurrencyServerPriceAPI api_price) {
        this.api_list = api_list;
        this.api_price = api_price;
    }

    private Observable<SandboxResponse<List<CryptoCurrencyItem>>> getResult(CryptoCurrencyList items_list, Map<String, CryptoCurrencyPrice> items_price) {

        return Observable.fromCallable(() -> {

            List<CryptoCurrencyItem> result = new ArrayList<>();

            for (String key : items_price.keySet()) {

                CryptoCurrencyList.Details details = items_list.data.get(key);

                if (details != null) result.add(new CryptoCurrencyItem(key, String.format(Locale.ENGLISH,"%.6f", items_price.get(key).price), items_list.base_url + details.image));

            }

            return new SandboxResponse<>(result);

        });

    }

    @Override
    public Observable<SandboxResponse<List<CryptoCurrencyItem>>> list() {

        return api_list.getCurrencyList()
                .flatMap(cryptoCurrencyList -> {

                    items_list = cryptoCurrencyList;

                    return api_price.getCurrencyPrice(items_list.toString(), "USD");

                }).flatMap(stringCryptoCurrencyPriceMap -> {

                    items_price = stringCryptoCurrencyPriceMap;

                return getResult(items_list, items_price);

            });

    }

    @Override
    public Observable<SandboxResponse<CryptoCurrencyItem>> byId(Long id) {
        return null;
    }

    @Override
    public Observable<SandboxResponse<List<CryptoCurrencyItem>>> save(List<CryptoCurrencyItem> list) {
        return null;
    }

    @Override
    public Observable<SandboxResponse<CryptoCurrencyItem>> save(CryptoCurrencyItem cryptoCurrencyItem) {
        return null;
    }

    @Override
    public Observable<SandboxResponse<Integer>> delete(Long id) {
        return null;
    }

    @Override
    public SandboxResponse<List<CryptoCurrencyItem>> saveSync(List<CryptoCurrencyItem> list) {
        return null;
    }

    @Override
    public SandboxResponse<CryptoCurrencyItem> saveSync(CryptoCurrencyItem cryptoCurrencyItem) {
        return null;
    }

}