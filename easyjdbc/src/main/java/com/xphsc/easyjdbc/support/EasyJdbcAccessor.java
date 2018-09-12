package com.xphsc.easyjdbc.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;

import javax.sql.DataSource;

/**
 * Created by ${huipei.x}
 */
public abstract class EasyJdbcAccessor implements InitializingBean {

    /** Logger available to subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    private DataSource dataSource;

    private SQLExceptionTranslator exceptionTranslator;

    private boolean lazyInit = true;

    private  String dialectName;
    /**
     * Set the JDBC DataSource to obtain connections from.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Return the DataSource used by this template.
     */
    public DataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * Specify the database product name for the DataSource that this accessor uses.
     * This allows to initialize a SQLErrorCodeSQLExceptionTranslator without
     * obtaining a Connection from the DataSource to get the metadata.
     * @param dbName the database product name that identifies the error codes entry
     * @see SQLErrorCodeSQLExceptionTranslator#setDatabaseProductName
     * @see java.sql.DatabaseMetaData#getDatabaseProductName()
     */
    public void setDatabaseProductName(String dbName) {
        this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dbName);
    }

    /**
     * Set the exception translator for this instance.
     * <p>If no custom translator is provided, a default
     * {@link SQLErrorCodeSQLExceptionTranslator} is used
     * which examines the SQLException's vendor-specific error code.
     * @see org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
     * @see org.springframework.jdbc.support.SQLStateSQLExceptionTranslator
     */
    public void setExceptionTranslator(SQLExceptionTranslator exceptionTranslator) {
        this.exceptionTranslator = exceptionTranslator;
    }

    /**
     * Return the exception translator for this instance.
     * <p>Creates a default {@link SQLErrorCodeSQLExceptionTranslator}
     * for the specified DataSource if none set, or a
     * {@link SQLStateSQLExceptionTranslator} in case of no DataSource.
     * @see #getDataSource()
     */
    public synchronized SQLExceptionTranslator getExceptionTranslator() {
        if (this.exceptionTranslator == null) {
            DataSource dataSource = getDataSource();
            if (dataSource != null) {
                this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            }
            else {
                this.exceptionTranslator = new SQLStateSQLExceptionTranslator();
            }
        }
        return this.exceptionTranslator;
    }

    /**
     * Set whether to lazily initialize the SQLExceptionTranslator for this accessor,
     * on first encounter of a SQLException. Default is "true"; can be switched to
     * "false" for initialization on startup.
     * <p>Early initialization just applies if {@code afterPropertiesSet()} is called.
     * @see #getExceptionTranslator()
     * @see #afterPropertiesSet()
     */
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    /**
     * Return whether to lazily initialize the SQLExceptionTranslator for this accessor.
     * @see #getExceptionTranslator()
     */
    public boolean isLazyInit() {
        return this.lazyInit;
    }

    /**
     * Eagerly initialize the exception translator, if demanded,
     * creating a default one for the specified DataSource if none set.
     */

    public void isTraceEnabledSql(String sql,Boolean isTraceEnabledSql){
        if(isTraceEnabledSql){
            logger.debug("sql语句:["+sql+"]");
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (getDataSource() == null) {
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }
        if (!isLazyInit()) {
            getExceptionTranslator();
        }
    }

    public String getDialectName() {
        return dialectName;
    }

    public void setDialectName(String dialectName) {
        this.dialectName = dialectName;
    }
}
