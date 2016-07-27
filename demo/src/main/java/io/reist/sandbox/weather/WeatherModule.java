package io.reist.sandbox.weather;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reist.sandbox.weather.model.WeatherService;
import io.reist.sandbox.weather.model.remote.WeatherServerApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

@Module
public class WeatherModule {

    public static final String BASE_URL = "http://api.apixu.com/v1/";

    @Provides
    @Singleton
    WeatherServerApi weatherApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(WeatherServerApi.class);
    }

    @Provides
    @Singleton
    WeatherService weatherService(WeatherServerApi api) {
        return new WeatherService(api);
    }
}
