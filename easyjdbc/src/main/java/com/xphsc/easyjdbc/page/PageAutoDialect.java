package com.xphsc.easyjdbc.page;


import com.xphsc.easyjdbc.core.exception.EasyJdbcException;
import com.xphsc.easyjdbc.dialect.*;
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
        //注册别名
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

    //获取当前的代理对象
    public AbstractDialect getDelegate() {
        if (delegate != null) {
            return delegate;
        }
        return dialectThreadLocal.get();
    }

    //移除代理对象
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
            throw new  EasyJdbcException("初始化  [" + dialectClass + "]时出错:" + e.getMessage(), e);
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
                throw new EasyJdbcException("无法自动获取数据库类型");
            }
        AbstractDialect dialect = initDialect(dialectStr);
            return dialect;

    }

}