package io.reist.visum;

import io.reist.visum.model.Error;
import io.reist.visum.model.ErrorImpl;
import io.reist.visum.model.Response;
import io.reist.visum.model.ResponseFactory;
import io.reist.visum.model.ResponseImpl;

/**
 * Created by m039 on 11/26/15.
 */
public class Visum {

    private Builder mBuilder;
    private ResponseFactory mResponseFactory;

    private Visum(Builder builder) {
        mBuilder = builder;
        mResponseFactory = new ResponseFactory(mBuilder.mResponseClass, mBuilder.mErrorClass);
    }

    public ResponseFactory responseFactory() {
        return mResponseFactory;
    }

    public static class Builder {

        Class<? extends Response> mResponseClass = ResponseImpl.class;
        Class<? extends Error> mErrorClass = ErrorImpl.class;

        /**
         * This function is needed if you have your custom Response class
         */
        public Builder setResponseClass(Class<? extends Response> responseClass) {
            mResponseClass = responseClass;
            return this;
        }

        /**
         * This function is needed if you have your custom Error class
         */
        public Builder setErrorClass(Class<? extends Error> errorClas) {
            mErrorClass = errorClas;
            return this;
        }

        public Visum build() {
            return new Visum(this);
        }

    }
}
