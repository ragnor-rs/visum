package io.reist.sandbox.weather.model.remote;

import io.reist.sandbox.weather.model.local.WeatherEntity;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

public interface WeatherServerApi {


    @GET("current.json")
    Single<WeatherEntity> loadWeather(@Query("key") String key,
                                      @Query("q") String query);


}
