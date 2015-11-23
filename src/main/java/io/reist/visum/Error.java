package io.reist.visum;

/**
 * Created by Reist on 23.11.15.
 */
public class Error {
    private final String message;
    private final Throwable throwable;

    public Error(String message) {
        this.message = message;
        this.throwable = null;
    }

    public Error(Throwable throwable) {
        this.throwable = throwable;
        this.message = null;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
