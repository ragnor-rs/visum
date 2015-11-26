package io.reist.visum.model;

import android.support.annotation.Nullable;

import java.lang.*;

/**
 * Created by Reist on 23.11.15.
 */
public class VisumError implements Error
{
    public VisumError() {
    }

    public VisumError(Throwable t) {
        throwable = t;
    }

    private String message;
    private Throwable throwable;

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
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
