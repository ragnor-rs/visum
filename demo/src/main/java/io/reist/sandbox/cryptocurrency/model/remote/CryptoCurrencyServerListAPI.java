package io.reist.sandbox.cryptocurrency.model.remote;

import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyList;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Sergey on 02/11/2017.
 */
public interface CryptoCurrencyServerListAPI {

    @GET("coinlist")
    Observable<CryptoCurrencyList> getCurrencyList();

}