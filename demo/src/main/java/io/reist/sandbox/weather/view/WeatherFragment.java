package io.reist.sandbox.weather.view;

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
        super(R.layout.fragment_weather);
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
        Snackbar.make(this.getView(), errorMessage , Snackbar.LENGTH_SHORT).show();
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
}
