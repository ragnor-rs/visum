package io.reist.visum.model;

import android.support.annotation.Nullable;

/**
 * Created by m039 on 11/26/15.
 */
public interface Error {

    @Nullable
    String getMessage();

    @Nullable
    Throwable getThrowable();

    void setMessage(String message);

    void setThrowable(Throwable throwable);

}
