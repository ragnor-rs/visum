package io.reist.sandbox.weather.model;

import java.util.List;

import javax.inject.Inject;

import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.SandboxService;
import io.reist.sandbox.weather.model.local.WeatherEntity;
import io.reist.sandbox.weather.model.remote.WeatherAPI;
import retrofit2.Call;
import rx.Observable;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

public class WeatherService implements SandboxService<WeatherEntity> {

    private static final String API_KEY = "74b3cd81c3dd453d9cf141407162507"; //TODO: ключ для апи, хреново тут его хранить

    WeatherAPI weatherAPI;

    @Inject
    public WeatherService(WeatherAPI api) {
        this.weatherAPI = api;
    }

    public Observable<WeatherEntity> getWeatherForCity(String city) {
        return weatherAPI.loadWeather(API_KEY, city);
    }

    public Call<WeatherEntity> getWeatherCall(String city) {
        return weatherAPI.loadWeatherCall(API_KEY, city);
    }

    @Override
    public Observable<SandboxResponse<List<WeatherEntity>>> list() {
        return null;
    }

    @Override
    public Observable<SandboxResponse<WeatherEntity>> byId(Long id) {
        return null;
    }

    @Override
    public Observable<SandboxResponse<List<WeatherEntity>>> save(List<WeatherEntity> list) {
        return null;
    }

    @Override
    public Observable<SandboxResponse<WeatherEntity>> save(WeatherEntity weatherEntity) {
        return null;
    }

    @Override
    public Observable<SandboxResponse<Integer>> delete(Long id) {
        return null;
    }

    @Override
    public SandboxResponse<List<WeatherEntity>> saveSync(List<WeatherEntity> list) {
        return null;
    }

    @Override
    public SandboxResponse<WeatherEntity> saveSync(WeatherEntity weatherEntity) {
        return null;
    }
}
