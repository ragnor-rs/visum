package io.reist.visum.model;

import com.google.gson.annotations.SerializedName;

import io.reist.visum.Error;

/**
 * Created by defuera on 09/11/2015.
 */
public class Response<T> {

    @SerializedName("data")
    private T data;

    private io.reist.visum.Error error;

    public Response(T t) {
        data = t;
    }

    public Response() {
    }

    public void setError(Error error) {
        this.error = error;
    }

    public boolean isSuccessful() {
        return error == null;
    }

    public Error getError() {
        return error;
    }

    public T getData() {
        return data;
    }

}

