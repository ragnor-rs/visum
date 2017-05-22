package io.reist.sandbox.result.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.reist.sandbox.BuildConfig;

import static android.os.Build.VERSION_CODES.M;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by defuera on 22/05/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = M
)
public class ResultActivityTest {

    private ResultActivity view;

    @Before
    public void setUp() throws Exception {
        view = Robolectric
                .buildActivity(ResultActivity.class)
                .create()
                .get();
    }

    @Test
    public void getPresenter() {
        assertThat(view.getPresenter()).isNotNull();
    }

}