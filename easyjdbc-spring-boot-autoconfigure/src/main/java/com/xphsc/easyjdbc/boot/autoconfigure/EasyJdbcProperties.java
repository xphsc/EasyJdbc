/*
 * Copyright (c) 2018-2019 huipei.x
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
package com.xphsc.easyjdbc.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${huipei.x}
 */

@ConfigurationProperties(prefix = EasyJdbcProperties.EASYJDBC_PREFIX)
public class EasyJdbcProperties {

    public static final String EASYJDBC_PREFIX = "easyjdbc";
    /**
     *Database dialect
     */
    private String dialect;
    /**
     *Scanning DAO Layer Interface
     */
    @Deprecated
    private String  basePackage;
    /**
     *Scanning DAO Layer Interface
     */
    private String  basePackages;

    /**
     *Setting up local cache
     */
    private boolean useLocalCache=true;
    /**
     *Setting Display SQL
     */
    private boolean showSql;

    private boolean enabled=true;


    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public boolean isUseLocalCache() {
        return useLocalCache;
    }

    public void setUseLocalCache(boolean useLocalCache) {
        this.useLocalCache = useLocalCache;
    }

    public boolean isShowSql() {
        return showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }

    public List<String> getInterceptorBeans() {
        return interceptorBeans;
    }

    public void setInterceptorBeans(List<String> interceptorBeans) {
        this.interceptorBeans = interceptorBeans == null ? new ArrayList<String>() : interceptorBeans;
    }
}

