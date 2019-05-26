package com.xphsc.easyjdbc.core.metadata.version;

/**
 * @author huipei.x
 * @date  2018-8-20
 * @description
 */
public class VersionException extends  RuntimeException {
    public VersionException() {
    }

    public VersionException(String message) {
        super(message);
    }

    public VersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public VersionException(Throwable cause) {
        super(cause);
    }
}