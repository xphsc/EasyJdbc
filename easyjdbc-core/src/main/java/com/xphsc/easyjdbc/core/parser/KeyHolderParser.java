package com.xphsc.easyjdbc.core.parser;

import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.Method;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: KeyHolderParser接口，用于解析获取主键Key
 */
public interface KeyHolderParser {

     Object getKey(Method method, KeyHolder keyHolder);

    Object getKey(Class<?> type, KeyHolder keyHolder);
    Object getKey(Method method,Number result);

}
