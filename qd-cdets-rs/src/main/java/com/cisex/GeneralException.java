package com.cisex;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 6/13/13
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeneralException extends RuntimeException {
    public GeneralException() {
        super();
    }

    public GeneralException(String msg) {
        super(msg);
    }

    public GeneralException(Throwable throwable) {
        super(throwable);
    }

    public GeneralException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
