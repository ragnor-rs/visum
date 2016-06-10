package io.reist.sandbox.result;

import javax.inject.Singleton;

import dagger.Subcomponent;
import io.reist.sandbox.result.view.ResultActivity;

/**
 * Created by Reist on 07.06.16.
 */
@Singleton
@Subcomponent
public interface ResultComponent {
    void inject(ResultActivity resultActivity);
}
