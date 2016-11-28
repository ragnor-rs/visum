package io.reist.sandbox.weather.view;

import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.weather.model.local.WeatherEntity;
import io.reist.sandbox.weather.presenter.WeatherPresenter;
import io.reist.visum.view.VisumView;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

public interface WeatherView extends VisumView<WeatherPresenter> {

    void showSuccess();

    void showError(SandboxError error);

    void showData(WeatherEntity entity);

    void showLoading(boolean enabled);
}
