package io.reist.sandbox.time;

import dagger.Module;
import dagger.Provides;
import io.reist.sandbox.time.model.TimeService;

/**
 * Created by Reist on 20.05.16.
 */
@Module
public class TimeModule {

    @Provides
    TimeService provideTimeService() {
        return new TimeService();
    }

}
