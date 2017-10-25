package com.fxlc.trucker.net;

/**
 * Created by cyd on 2017/7/20.
 */

public class MyThrowable extends Throwable {
    public MyThrowable( String errorCode) {

        this.errorCode = errorCode;
    }

    private String errorCode;
    @Override
    public String getMessage() {
        return " response data error";
    }

    public String getErrorCode(){

        return errorCode;

    }
}
