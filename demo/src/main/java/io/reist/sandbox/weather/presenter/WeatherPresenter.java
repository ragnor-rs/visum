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

package io.reist.sandbox.weather.presenter;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.weather.model.WeatherService;
import io.reist.sandbox.weather.model.local.WeatherEntity;
import io.reist.sandbox.weather.view.WeatherView;
import io.reist.visum.presenter.VisumPresenter;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

@Singleton
public class WeatherPresenter extends VisumPresenter<WeatherView> {

    public static final int WEATHER_CODE = 1001;

    private String TAG = WeatherPresenter.class.getName();
    private WeatherService weatherService;
    private boolean isLoading;

    @Inject
    public WeatherPresenter(WeatherService service) {
        this.weatherService = service;
    }

    @Override
    protected void onViewAttached() {
        super.onViewAttached();
    }

    public void loadData(String query) {
        if (isLoading) return;

        isLoading = true;
        view(WEATHER_CODE).showLoading(true);

        weatherService.getWeatherForCity(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherSubscriber);
    }

    private SingleSubscriber<WeatherEntity> weatherSubscriber = new SingleSubscriber<WeatherEntity>() {
        @Override
        public void onSuccess(WeatherEntity value) {

            if (value == null) {
                onError(new NullPointerException("Entity is null"));
                return;
            }

            view(WEATHER_CODE).showLoading(false);
            view(WEATHER_CODE).showData(value);
            isLoading = false;
        }

        @Override
        public void onError(Throwable e) {
            SandboxError error = new SandboxError(e);
            view(WEATHER_CODE).showError(error);
            view(WEATHER_CODE).showLoading(false);
            isLoading = false;
        }
    };
}
