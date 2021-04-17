/*
 * Copyright (c) 2021  huipei.x
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xphsc.easyjdbc.core.metadata.type;

import com.xphsc.easyjdbc.core.exception.EasyJdbcException;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link BaseTypeHandler}
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description:
 * @since 2.1.0
 */
public final class TypeBinding {

    private static Map<Class<?>, Class<?>> typeAliasMap = new HashMap<Class<?>, Class<?>>();
    private BaseTypeHandler delegate;
    private ThreadLocal<BaseTypeHandler> typeThreadLocal = new ThreadLocal<BaseTypeHandler>();
    private static Map<Class<?>, Class<?>> equalTypeMap = new HashMap<Class<?>, Class<?>>();
    static {
        /**
         *   Registration alias
         */
        typeAliasMap.put(LocalDate.class, LocalDateTypeHandler.class);
        typeAliasMap.put(LocalDateTime.class, LocalDateTimeTypeHandler.class);
        typeAliasMap.put(LocalTime.class, LocalTimeTypeHandler.class);
        typeAliasMap.put(Instant.class, InstantTypeHandler.class);
        typeAliasMap.put(OffsetDateTime.class, OffsetDateTimeTypeHandler.class);
        typeAliasMap.put(OffsetTime.class, OffsetTimeTypeHandler.class);
        typeAliasMap.put(Year.class, YearTypeHandler.class);

    }
    private boolean autoDialect = true;

    public void initDelegateType(Class dialectName) {
        if (delegate == null) {
            if (autoDialect) {
                this.delegate = getType(dialectName);
            } else {
                typeThreadLocal.set(getType(dialectName));
            }
        }
    }

    public static Class type(Class<?> dialectName) {
        for (Class jdbcType : typeAliasMap.keySet()) {
            if (dialectName.equals(jdbcType)) {
                return jdbcType;
            }
        }
        return null;
    }


    /**
     *Get the current proxy object
     */
    public BaseTypeHandler getDelegate() {
        if (delegate != null) {
            return delegate;
        }

        return typeThreadLocal.get();
    }



    /**
     *Remove proxy objects
     */
    public void clearDelegate() {
        typeThreadLocal.remove();
    }

    private BaseTypeHandler initType(Class<?> dialectClass) {
        BaseTypeHandler jdbcType = null;
        try {
            Class sqlDialectClass = resloveDialectClass(dialectClass);
            if (BaseTypeHandler.class.isAssignableFrom(sqlDialectClass)) {
                jdbcType = (BaseTypeHandler) sqlDialectClass.newInstance();
            }
        } catch (Exception e) {
            throw new EasyJdbcException("Initialization  [" + dialectClass + "]Error occurred:" + e.getMessage(), e);
        }

        return jdbcType;
    }

    private Class resloveDialectClass(Class<?> className) throws Exception {
        if (typeAliasMap.containsKey(className)) {
            return typeAliasMap.get(className);
        } else {
            return Class.forName(className.getName());
        }
    }

    private BaseTypeHandler getType(Class<?> dialectName) {

        Class jdbcType = type(dialectName);
        if (jdbcType == null) {
            throw new EasyJdbcException("Unable to automatically retrieve database type");
        }
        BaseTypeHandler type = initType(jdbcType);
        return type;

    }

}
