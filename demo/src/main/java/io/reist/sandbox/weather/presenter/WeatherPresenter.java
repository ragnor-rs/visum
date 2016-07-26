package io.reist.sandbox.weather.presenter;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.model.SandboxService;
import io.reist.sandbox.weather.model.WeatherService;
import io.reist.sandbox.weather.model.local.WeatherEntity;
import io.reist.sandbox.weather.view.WeatherView;
import io.reist.visum.presenter.VisumPresenter;
import rx.Observer;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

@Singleton
public class WeatherPresenter extends VisumPresenter<WeatherView> {

    public static final int WEATHER_CODE = 1001;

    private final String TAG = WeatherPresenter.class.getName();
    private SandboxService weatherService;

    @Inject
    public WeatherPresenter(WeatherService service) {
        this.weatherService = service;
    }

    @Override
    protected void onViewAttached() {
        super.onViewAttached();
    }

    public void loadData(String query) {
        view(WEATHER_CODE).showLoading(true);

        subscribe(WEATHER_CODE, ((WeatherService) weatherService).getWeatherForCity(query), new Observer<WeatherEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                SandboxError error = new SandboxError(e);
                view(WEATHER_CODE).showError(error);
                view(WEATHER_CODE).showLoading(false);
            }

            @Override
            public void onNext(WeatherEntity result) {
                view(WEATHER_CODE).showLoading(false);
                if (result == null) return;
                view(WEATHER_CODE).showData(result);
            }
        });

//        Call<WeatherEntity> call = ((WeatherService)weatherService).getWeatherCall(query);
//        call.enqueue(new Callback<WeatherEntity>() {
//            @Override
//            public void onResponse(Call<WeatherEntity> call, Response<WeatherEntity> response) {
//                int i = 0;
//            }
//
//            @Override
//            public void onFailure(Call<WeatherEntity> call, Throwable t) {
//                int i = 0;
//            }
//        });
    }
}
