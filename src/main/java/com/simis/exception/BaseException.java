package com.simis.exception;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public abstract class BaseException extends RuntimeException {
    private static final long serialVersionUID = -6189239714762326880L;

    public BaseException() {
    }

    public BaseException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public BaseException(String arg0) {
        super(arg0);
    }

    public BaseException(Throwable arg0) {
        super(arg0);
    }
}
