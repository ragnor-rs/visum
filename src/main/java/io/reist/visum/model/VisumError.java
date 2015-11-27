package io.reist.visum.model;

import android.support.annotation.Nullable;

import java.lang.*;

/**
 * Created by Reist on 23.11.15.
 */
public class VisumError implements Error
{

    private String message;
    private Throwable throwable;

    public VisumError(String message) {
        this.message = message;
    }

    public VisumError(Throwable t) {
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
