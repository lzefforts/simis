package com.simis.exception;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public class ApplicationException extends BaseException {
    private static final long serialVersionUID = -1257183185015676506L;

    public ApplicationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ApplicationException(String arg0) {
        super(arg0);
    }

    public ApplicationException(Throwable arg0) {
        super(arg0);
    }
}

