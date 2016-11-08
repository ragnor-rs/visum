package io.reist.sandbox.feed.model;

import android.os.Build;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import io.reist.sandbox.BuildConfig;
import io.reist.sandbox.core.RobolectricTestCase;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class FeedServiceTest extends RobolectricTestCase{

    @Inject
    FeedService feedService;
}
