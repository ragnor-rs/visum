package io.reist.visum.model;

import android.support.annotation.Nullable;

/**
 * Created by Reist on 23.11.15.
 */
public class BaseError implements Error {

    private String message;
    private Throwable throwable;

    public BaseError(String message) {
        this.message = message;
    }

    public BaseError(Throwable t) {
        throwable = t;
    }

    @Nullable
    @Override
    public String getMessage() {
        return message;
    }

    @Nullable
    @Override
    public Throwable getThrowable() {
        return throwable;
    }

}
