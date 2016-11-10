package io.reist.sandbox.core;

import org.robolectric.shadows.ShadowLog;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * Created by m039 on 12/1/15.
 */
public class RobolectricTestCase {

    private final RxJavaSchedulersHook rxJavaSchedulersHook = new RxJavaSchedulersHook() {
        @Override
        public Scheduler getIOScheduler() {
            return Schedulers.immediate();
        }

        @Override
        public Scheduler getNewThreadScheduler() {
            return Schedulers.immediate();
        }
    };

    private final RxAndroidSchedulersHook rxAndroidSchedulersHook = new RxAndroidSchedulersHook() {
        @Override
        public Scheduler getMainThreadScheduler() {
            return Schedulers.immediate();
        }
    };

    public void setup() {
        ShadowLog.stream = System.out;

        RxAndroidPlugins.getInstance().reset();
        RxAndroidPlugins.getInstance().registerSchedulersHook(rxAndroidSchedulersHook);

        RxJavaPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().registerSchedulersHook(rxJavaSchedulersHook);
    }

    public void end() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }
}
