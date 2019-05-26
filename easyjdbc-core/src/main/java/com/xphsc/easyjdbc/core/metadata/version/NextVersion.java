package com.xphsc.easyjdbc.core.metadata.version;

/**
 * @author huipei.x
 * @date  2018-8-20
 * @description
 */
public interface NextVersion<T> {
    T nextVersion(T var1) throws VersionException;
}
