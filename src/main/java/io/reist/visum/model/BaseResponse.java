package io.reist.visum.model;

import android.support.annotation.Nullable;

/**
 * Created by defuera on 09/11/2015.
 */
public class BaseResponse<T> implements Response<T> {

    private T mResult;
    private Error mError;

    public BaseResponse(T result) {
        mResult = result;
    }

    public BaseResponse(Error error) {
        mError = error;
    }

    @Override
    public boolean isSuccessful() {
        return mError == null;
    }

    @Nullable
    @Override
    public Error getError() {
        return mError;
    }

    @Nullable
    @Override
    public T getResult() {
        return mResult;
    }

}
