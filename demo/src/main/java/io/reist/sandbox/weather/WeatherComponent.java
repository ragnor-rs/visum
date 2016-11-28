package io.reist.sandbox.weather;

import dagger.Subcomponent;
import io.reist.sandbox.weather.view.WeatherFragment;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

@Subcomponent
public interface WeatherComponent {
    void inject(WeatherFragment weatherFragment);
}
