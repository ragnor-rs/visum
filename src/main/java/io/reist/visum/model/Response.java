package io.reist.visum.model;

import com.google.gson.annotations.SerializedName;

import io.reist.visum.Error;

/**
 * Created by defuera on 09/11/2015.
 */
public class Response<T> {

    @SerializedName("result")
    private T result;

    private Error error;

    public Response() {}

    public Response(T t) {
        result = t;
    }

    public Response(Error e) {
        error = e;
    }

    public boolean isSuccessful() {
        return error == null;
    }

    public Error getError() {
        return error;
    }

    public T getResult() {
        return result;
    }

}

