package io.reist.sandbox.time.model;

import rx.Single;

/**
 * Created by Reist on 20.05.16.
 */
public class TimeService {
    public Single<Long> getTime() {
        return Single.fromCallable(() -> {
            Thread.sleep(1000);
            return System.currentTimeMillis();
        });
    }
}
