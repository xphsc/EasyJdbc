package com.xphsc.easyjdbc.core.exception;

/**
 * Created by ${huipei.x}
 */
public class EasyJdbcException extends RuntimeException {
    public EasyJdbcException() {
        super();
    }

    public EasyJdbcException(String message) {
        super(message);
    }

    public EasyJdbcException(String message, Throwable cause) {
        super(message, cause);
    }

    public EasyJdbcException(Throwable cause) {
        super(cause);
    }
}
