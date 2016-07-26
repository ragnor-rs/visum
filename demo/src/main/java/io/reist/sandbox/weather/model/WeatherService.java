package io.reist.sandbox.weather.model;

import javax.inject.Inject;

import io.reist.sandbox.weather.model.local.WeatherEntity;
import io.reist.sandbox.weather.model.remote.WeatherAPI;
import rx.Single;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

public class WeatherService {

    private String API_KEY = "74b3cd81c3dd453d9cf141407162507";

    WeatherAPI weatherAPI;

    @Inject
    public WeatherService(WeatherAPI api) {
        this.weatherAPI = api;
    }

    public Single<WeatherEntity> getWeatherForCity(String city) {
        return weatherAPI.loadWeather(API_KEY, city);
    }
}
