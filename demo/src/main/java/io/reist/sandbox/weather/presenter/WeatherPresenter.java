package io.reist.sandbox.weather.presenter;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.weather.model.WeatherService;
import io.reist.sandbox.weather.model.local.WeatherEntity;
import io.reist.sandbox.weather.view.WeatherView;
import io.reist.visum.presenter.VisumViewPresenter;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

@Singleton
public class WeatherPresenter extends VisumViewPresenter<WeatherView> {

    private WeatherService weatherService;
    private WeatherEntity weatherEntity;
    private boolean isLoading;

    @Inject
    public WeatherPresenter(WeatherService service) {
        this.weatherService = service;
    }

    @Override
    protected void onViewAttached(@NonNull WeatherView view) {
        if (weatherEntity != null) {
            view.showData(weatherEntity);
        }
    }

    public void loadData(String query) {
        if (isLoading) return;

        isLoading = true;
        view().showLoading(true);

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
            weatherEntity = value;
            view().showLoading(false);
            view().showData(value);
            isLoading = false;
        }

        @Override
        public void onError(Throwable e) {
            SandboxError error = new SandboxError(e);
            view().showError(error);
            view().showLoading(false);
            isLoading = false;
        }
    };
}
