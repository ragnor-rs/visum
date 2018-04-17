package io.reist.sandbox.cryptocurrency.model.remote;

import java.util.Map;

import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyPrice;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Sergey on 02/11/2017.
 */
public interface CryptoCurrencyServerPriceAPI {

    @GET("pricemulti")
    Observable<Map<String, CryptoCurrencyPrice>> getCurrencyPrice(@Query("fsyms") String fsyms, @Query("tsyms") String tsyms);

}