package io.github.karlatemp.mxlib.exception;

public class ValueNotInitializedException extends RuntimeException {
    public ValueNotInitializedException(String msg) {
        super(msg);
    }

    public ValueNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueNotInitializedException(Throwable cause) {
        super(cause);
    }

    public ValueNotInitializedException() {
    }
}
