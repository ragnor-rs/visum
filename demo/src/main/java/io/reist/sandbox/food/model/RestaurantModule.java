package io.reist.sandbox.food.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reist.sandbox.food.model.local.RestaurantsService;
import io.reist.sandbox.food.model.remote.RestaurantApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */


@Module
public class RestaurantModule {

    @Provides
    @Singleton
    RestaurantApi restaurantApi() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

       return retrofit.create(RestaurantApi.class);
    }


    @Provides
    @Singleton
    RestaurantsService restaurantsService( StorIOSQLite storIOSQLite) {
        return new RestaurantsService(storIOSQLite);
    }

    @Provides
    @Singleton
    RestaurantMonitor restaurantMonitor(StorIOSQLite storIOSQLite) {
        return new RestaurantMonitor(restaurantApi(), restaurantsService(storIOSQLite));
    }
}
