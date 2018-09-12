package com.xphsc.easyjdbc.core.exception;

/**
 * Created by ${huipei.x} on 2018-7-1.
 */
public class JdbcDataException extends NestedRuntimeException {

    /**
     * Constructor for DataAccessException.
     * @param msg the detail message
     */
    public JdbcDataException(String msg) {
        super(msg);
    }

    /**
     * Constructor for DataAccessException.
     * @param msg the detail message
     * @param cause the root cause (usually from using a underlying
     * data access API such as JDBC)
     */
    public JdbcDataException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
