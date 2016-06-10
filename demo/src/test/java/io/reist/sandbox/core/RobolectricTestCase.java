package io.reist.sandbox.core;

import org.robolectric.shadows.ShadowLog;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * Created by m039 on 12/1/15.
 */
public class RobolectricTestCase {

    public void setUp() {
        ShadowLog.stream = System.out;

        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }
}
