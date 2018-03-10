package io.reist.sandbox.cryptocurrency;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reist.sandbox.cryptocurrency.model.CryptoCurrencyBaseService;
import io.reist.sandbox.cryptocurrency.model.CryptoCurrencyCachedService;
import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyLocalService;
import io.reist.sandbox.cryptocurrency.model.remote.CryptoCurrencyRemoteService;
import io.reist.sandbox.cryptocurrency.model.remote.CryptoCurrencyServerListAPI;
import io.reist.sandbox.cryptocurrency.model.remote.CryptoCurrencyServerPriceAPI;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sergey on 02/11/2017.
 */
@Module
public final class CryptoCurrencyModule {

    private static final String URL_LIST = "https://www.cryptocompare.com/api/data/";
    private static final String URL_PRICE = "https://min-api.cryptocompare.com/data/";

    @Provides
    @Singleton
    CryptoCurrencyServerListAPI apiGetList() {

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL_LIST)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

        return retrofit.create(CryptoCurrencyServerListAPI.class);

    }

    @Provides
    @Singleton
    CryptoCurrencyServerPriceAPI apiGetPrice() {

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL_PRICE)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

        return retrofit.create(CryptoCurrencyServerPriceAPI.class);

    }

    @Singleton
    @Provides
    CryptoCurrencyBaseService cachedService(StorIOSQLite storIOSQLite, CryptoCurrencyServerListAPI listAPI, CryptoCurrencyServerPriceAPI priceAPI) {
        return new CryptoCurrencyCachedService(new CryptoCurrencyLocalService(storIOSQLite), new CryptoCurrencyRemoteService(listAPI, priceAPI));
    }

}