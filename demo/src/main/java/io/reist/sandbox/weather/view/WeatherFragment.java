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

package io.reist.sandbox.weather.view;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reist.sandbox.R;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.weather.WeatherComponent;
import io.reist.sandbox.weather.model.local.WeatherEntity;
import io.reist.sandbox.weather.presenter.WeatherPresenter;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

public class WeatherFragment extends BaseFragment<WeatherPresenter> implements WeatherView {

    @BindView(R.id.city_text)
    EditText city;

    @BindView(R.id.find)
    Button findButton;

    @BindView(R.id.temp_text)
    TextView temp;

    @BindView(R.id.loading_progress)
    ProgressBar loadingProgress;

    @BindView(R.id.temp_layout)
    RelativeLayout results;

    @BindView(R.id.pressure_text)
    TextView pressure;

    @Inject
    WeatherPresenter presenter;

    public WeatherFragment() {
        super(WeatherPresenter.WEATHER_CODE, R.layout.fragment_weather);
    }

    @Override
    public void attachPresenter() {
        super.attachPresenter();
    }

    @Override
    public void showSuccess() {
        Snackbar.make(this.getView(), "Success", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showError(SandboxError error) {
        String errorMessage = error.getMessage() == null ? getString(R.string.weather_error) : error.getMessage();
        Snackbar.make(this.getView(), errorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showData(WeatherEntity entity) {
        temp.setText(String.valueOf(entity.getTemperature()));
        pressure.setText(String.valueOf(entity.getPressure()));
    }

    @Override
    public void showLoading(boolean enabled) {
        loadingProgress.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
        results.setVisibility(!enabled ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public WeatherPresenter getPresenter() {
        return presenter;
    }

    @OnClick(R.id.find)
    void onFindClick() {
        presenter.loadData(city.getText().toString().trim());
    }

    @Override
    public void inject(@NonNull Object component) {
        ((WeatherComponent) component).inject(this);
    }

}
