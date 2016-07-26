package io.reist.sandbox.weather.model;

import javax.inject.Inject;

import io.reist.sandbox.weather.model.local.WeatherEntity;
import io.reist.sandbox.weather.model.remote.WeatherServerApi;
import rx.Single;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

public class WeatherService {

    private String API_KEY = "74b3cd81c3dd453d9cf141407162507";

    WeatherServerApi weatherServerApi;

    @Inject
    public WeatherService(WeatherServerApi api) {
        this.weatherServerApi = api;
    }

    public Single<WeatherEntity> getWeatherForCity(String city) {
        return weatherServerApi.loadWeather(API_KEY, city);
    }
}
