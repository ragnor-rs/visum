package io.reist.sandbox.cryptocurrency.model.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.cryptocurrency.model.CryptoCurrencyBaseService;
import io.reist.sandbox.app.model.CryptoCurrencyItem;
import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyList;
import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyPrice;
import rx.Observable;

/**
 * Created by Sergey on 03/11/2017.
 */
public final class CryptoCurrencyRemoteService implements CryptoCurrencyBaseService {

    private CryptoCurrencyServerListAPI apiList;
    private CryptoCurrencyServerPriceAPI apiPrice;

    private CryptoCurrencyList itemsList;
    private Map<String, CryptoCurrencyPrice> itemsPrice;

    public CryptoCurrencyRemoteService(CryptoCurrencyServerListAPI apiList, CryptoCurrencyServerPriceAPI apiPrice) {
        this.apiList = apiList;
        this.apiPrice = apiPrice;
    }

    private Observable<SandboxResponse<List<CryptoCurrencyItem>>> getResult(CryptoCurrencyList itemsList, Map<String, CryptoCurrencyPrice> itemsPrice) {
        return Observable.fromCallable(() -> {

            List<CryptoCurrencyItem> result = new ArrayList<>();

            for (String key : itemsPrice.keySet()) {

                CryptoCurrencyList.Details details = itemsList.data.get(key);

                if (details != null) result.add(new CryptoCurrencyItem(key, String.format(Locale.ENGLISH,"%.6f", itemsPrice.get(key).price), itemsList.baseUrl + details.image));

            }

            return new SandboxResponse<>(result);

        });
    }

    @Override
    public Observable<SandboxResponse<List<CryptoCurrencyItem>>> list() {
        return apiList.getCurrencyList()
                .flatMap(cryptoCurrencyList -> {

                    itemsList = cryptoCurrencyList;

                    return apiPrice.getCurrencyPrice(itemsList.toString(), "USD");

                }).flatMap(stringCryptoCurrencyPriceMap -> {

                    itemsPrice = stringCryptoCurrencyPriceMap;

                return getResult(itemsList, itemsPrice);

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