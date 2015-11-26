package io.reist.visum.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by defuera on 09/11/2015.
 */
public class ResponseImpl<T> implements Response<T>
{
    private T mResult;
    private Error mError;

    public ResponseImpl() {
    }

    public ResponseImpl(Response<T> response) {
        mResult = response.getResult();
        mError = response.getError();
    }

    @Override
    public boolean isSuccessful() {
        return mError == null;
    }

    @Override
    public void setError(Error error) {
        this.mError = error;
    }

    @Override
    public void setResult(T result) {
        this.mResult = result;
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

