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
