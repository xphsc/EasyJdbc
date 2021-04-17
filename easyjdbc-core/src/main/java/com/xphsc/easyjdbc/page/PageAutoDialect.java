/*
 * Copyright (c) 2018 huipei.x
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
package com.xphsc.easyjdbc.page;


import com.xphsc.easyjdbc.core.exception.EasyJdbcException;
import com.xphsc.easyjdbc.page.dialect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${huipei.x}
 */
public class PageAutoDialect {

    private static Map<String, Class<?>> dialectAliasMap = new HashMap<String, Class<?>>();
    private AbstractDialect delegate;
    private ThreadLocal<AbstractDialect> dialectThreadLocal = new ThreadLocal<AbstractDialect>();
    static {
        /**
         *   Registration alias
         */
        dialectAliasMap.put(DialectAlias.HSQLDB, HsqldbDialect.class);
        dialectAliasMap.put(DialectAlias.H2, HsqldbDialect.class);
        dialectAliasMap.put(DialectAlias.POSTGRESQL, HsqldbDialect.class);
        dialectAliasMap.put(DialectAlias.MYSQL, MySqlDialect.class);
        dialectAliasMap.put(DialectAlias.MARIADB, MySqlDialect.class);
        dialectAliasMap.put(DialectAlias.SQLITE, MySqlDialect.class);
        dialectAliasMap.put(DialectAlias.ORACLE, OracleDialect.class);
        dialectAliasMap.put(DialectAlias.DB2, Db2Dialect.class);
        dialectAliasMap.put(DialectAlias.SQLSERVER, SqlServerDialect.class);
        dialectAliasMap.put(DialectAlias.INFORMIX, InformixDialect.class);
        dialectAliasMap.put(DialectAlias.PHOENIX, HsqldbDialect.class);
        dialectAliasMap.put(DialectAlias.HERDDB, HerdDBDialect.class);
        dialectAliasMap.put(DialectAlias.DM, OracleDialect.class);
        dialectAliasMap.put(DialectAlias.EDB, OracleDialect.class);
        dialectAliasMap.put(DialectAlias.OSCAR, MySqlDialect.class);
        dialectAliasMap.put(DialectAlias.CLICKHOUSE, MySqlDialect.class);

    }
    private boolean autoDialect = true;

    public void initDelegateDialect(String dialectName) {
        if (delegate == null) {
            if (autoDialect) {
                this.delegate = getDialect(dialectName);
            } else {
                dialectThreadLocal.set(getDialect(dialectName));
            }
        }
    }
    public static String dialect(String dialectName) {
        for (String dialect : dialectAliasMap.keySet()) {
            if (dialectName.toLowerCase().equals(dialect)) {
                return dialect;
            }
        }
        return null;
    }


    /**
     *Get the current proxy object
     */
    public AbstractDialect getDelegate() {
        if (delegate != null) {
            return delegate;
        }
        return dialectThreadLocal.get();
    }



    /**
     *Remove proxy objects
     */
    public void clearDelegate() {
        dialectThreadLocal.remove();
    }

    private AbstractDialect initDialect(String dialectClass) {
        AbstractDialect dialect = null;
        try {
            Class sqlDialectClass = resloveDialectClass(dialectClass);
            if (AbstractDialect.class.isAssignableFrom(sqlDialectClass)) {
                dialect = (AbstractDialect) sqlDialectClass.newInstance();
            }
        } catch (Exception e) {
            throw new  EasyJdbcException("Initialization  [" + dialectClass + "]Error occurred:" + e.getMessage(), e);
        }
        return dialect;
    }

    private Class resloveDialectClass(String className) throws Exception {
        if (dialectAliasMap.containsKey(className.toLowerCase())) {
            return dialectAliasMap.get(className.toLowerCase());
        } else {
            return Class.forName(className);
        }
    }

    private AbstractDialect getDialect(String dialectName) {

            String dialectStr = dialect( dialectName);
            if (dialectStr == null) {
                throw new EasyJdbcException("Unable to automatically retrieve database type");
            }
        AbstractDialect dialect = initDialect(dialectStr);
            return dialect;

    }

}