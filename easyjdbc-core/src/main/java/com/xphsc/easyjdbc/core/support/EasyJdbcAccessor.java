/*
 * Copyright (c) 2018-2019  huipei.x
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
package com.xphsc.easyjdbc.core.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;

import javax.sql.DataSource;

/**
 * Created by ${huipei.x}
 */
public abstract class EasyJdbcAccessor implements InitializingBean {



    private DataSource dataSource;

    private SQLExceptionTranslator exceptionTranslator;

    private boolean lazyInit = true;
    /**
     *Database dialect
     */
    private  String dialectName;

    private JdbcTemplate jdbcTemplate;
    /**
     *Setting up local cache
     */
    private boolean useLocalCache;
    /**
     *Setting Display SQL
     */
    private boolean showSQL;

    private String   interfaceClass;

    /**
     * Set the JDBC DataSource to obtain connections from.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        if(jdbcTemplate!=null){
            jdbcTemplate=new JdbcTemplate(dataSource);
        }
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
    protected void setDatabaseProductName(String dbName) {
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
    protected void setExceptionTranslator(SQLExceptionTranslator exceptionTranslator) {
        this.exceptionTranslator = exceptionTranslator;
    }

    /**
     * Return the exception translator for this instance.
     * <p>Creates a default {@link SQLErrorCodeSQLExceptionTranslator}
     * for the specified DataSource if none set, or a
     * {@link SQLStateSQLExceptionTranslator} in case of no DataSource.
     * @see #getDataSource()
     */
    protected synchronized SQLExceptionTranslator getExceptionTranslator() {
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
    protected void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    /**
     * Return whether to lazily initialize the SQLExceptionTranslator for this accessor.
     * @see #getExceptionTranslator()
     */
    protected boolean isLazyInit() {
        return this.lazyInit;
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
    protected void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        if(jdbcTemplate.getDataSource()!=null){
            this.dataSource=jdbcTemplate.getDataSource();
        }

    }

    public JdbcBuilder getJdbcBuilder(){
        JdbcBuilder jdbcBuilder=new JdbcBuilder(this.getJdbcTemplate(),useLocalCache, showSQL,interfaceClass!=null?interfaceClass:"com.xphsc.easyjdbc.EasyJdbcTemplate");
        return jdbcBuilder;
    }
    /**
     * 获取jdbcTemplate
     */
    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    public boolean showSQL(boolean showSQL){
        return this.showSQL=showSQL;
    };
    public boolean useLocalCache(boolean useLocalCache){
        return this.useLocalCache=useLocalCache;
    };

    public void   interfaceClass(String interfaceClass){
        this.interfaceClass=interfaceClass;
    };


}
