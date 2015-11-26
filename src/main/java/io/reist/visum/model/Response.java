package io.reist.visum.model;

import android.support.annotation.Nullable;

/**
 * Created by m039 on 11/26/15.
 */
public interface Response<R> {

    @Nullable
    R getResult();

    @Nullable
    Error getError();

    boolean isSuccessful();

}
