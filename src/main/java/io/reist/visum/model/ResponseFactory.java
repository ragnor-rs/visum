package io.reist.visum.model;

/**
 * Created by m039 on 11/26/15.
 */
public class ResponseFactory {

    Class<? extends Response> mResponseClass = VisumResponse.class;
    Class<? extends Error> mErrorClass = VisumError.class;

    public ResponseFactory(Class<? extends Response> responseClass, Class<? extends Error> errorClass) {
        mResponseClass = responseClass;
        mErrorClass = errorClass;
    }

    public <T> Response<T> newErrorResponse(Throwable throwable) {
        Response<T> response = newResponseInstance(mResponseClass);
        Error error = newErrorInstance(mErrorClass);

        error.setThrowable(throwable);

        response.setError(error);

        return response;
    }

    public <T> Response<T> newErrorResponse(String message) {
        Response<T> response = newResponseInstance(mResponseClass);
        Error error = newErrorInstance(mErrorClass);

        error.setMessage(message);

        response.setError(error);

        return response;
    }

    public <T> Response<T> newResultResponse(T result) {
        Response<T> response = newResponseInstance(mResponseClass);

        response.setResult(result);

        return response;
    }

    static <T> Error newErrorInstance(Class<? extends Error> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * It should work unless we encounter custom behaviour
     */
    @SuppressWarnings("unchecked")
    static <T> Response<T> newResponseInstance(Class<? extends Response> clazz) {
        try {
            return (Response<T>) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
