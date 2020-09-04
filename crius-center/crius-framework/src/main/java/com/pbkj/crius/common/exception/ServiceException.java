package com.pbkj.crius.common.exception;


/**
 * @author GZQ
 * 自定义异常处理类
 */
public class ServiceException extends RuntimeException {

    private int code;

    public ServiceException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.code = errorCode;
    }

    public int getCode() {
        return code;
    }
}
