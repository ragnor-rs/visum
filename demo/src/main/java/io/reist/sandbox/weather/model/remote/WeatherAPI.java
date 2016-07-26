package io.reist.sandbox.weather.model.remote;

import io.reist.sandbox.weather.model.local.WeatherEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

public interface WeatherAPI {

    @GET("current.json")
    Observable<WeatherEntity> loadWeather(@Query("key") String key,
                                                           @Query("q") String query);
    @GET("current.json")
    Call<WeatherEntity> loadWeatherCall(@Query("key") String key,
                                    @Query("q") String query);
}
