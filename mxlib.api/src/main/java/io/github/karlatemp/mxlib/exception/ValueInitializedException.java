package io.github.karlatemp.mxlib.exception;

public class ValueInitializedException extends RuntimeException {
    public ValueInitializedException(String msg) {
        super(msg);
    }

    public ValueInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueInitializedException(Throwable cause) {
        super(cause);
    }

    public ValueInitializedException() {
    }
}
