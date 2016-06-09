package io.reist.sandbox.time;

import javax.inject.Singleton;

import dagger.Subcomponent;
import io.reist.sandbox.time.view.TimeFragment;
import io.reist.sandbox.time.view.TimeNotification;

/**
 * Created by Reist on 20.05.16.
 */
@Singleton
@Subcomponent
public interface TimeComponent {
    void inject(TimeFragment timeFragment);

    void inject(TimeNotification timeNotification);
}
