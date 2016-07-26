package io.reist.sandbox.weather.model;

import javax.inject.Inject;

import io.reist.sandbox.weather.model.local.WeatherEntity;
import io.reist.sandbox.weather.model.remote.WeatherApi;
import rx.Single;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

public class WeatherService {

    private String API_KEY = "74b3cd81c3dd453d9cf141407162507";

    WeatherApi weatherApi;

    @Inject
    public WeatherService(WeatherApi api) {
        this.weatherApi = api;
    }

    public Single<WeatherEntity> getWeatherForCity(String city) {
        return weatherApi.loadWeather(API_KEY, city);
    }
}
