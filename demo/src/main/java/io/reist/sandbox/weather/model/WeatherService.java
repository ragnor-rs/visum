/*
 * Copyright (C) 2017 Renat Sarymsakov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
