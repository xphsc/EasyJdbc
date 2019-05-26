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
import com.xphsc.easyjdbc.core.cache.Cache;
import com.xphsc.easyjdbc.core.cache.CacheKey;
import com.xphsc.easyjdbc.core.cache.PerpetualCache;
import com.xphsc.easyjdbc.core.cache.SimpleCachekeyBuiler;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.KeyHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author huipei.x
 * @date  2019-3-18
 * @description
 */
public class JdbcBuilder extends SimpleCachekeyBuiler {



    private JdbcTemplate jdbcTemplate;

    private static Cache  cache ;

    private boolean useLocalCache;

    private boolean showSQL;

    protected  Log logger;
    public JdbcBuilder(JdbcTemplate jdbcTemplate, boolean useLocalCache, boolean showSQL,String interfaceClass) {
        this.jdbcTemplate = jdbcTemplate;
        this.useLocalCache=useLocalCache;
        this.showSQL=showSQL;

         logger = LogFactory.getLog(interfaceClass);

    }

    private static Cache getCacheInstance(){
        if(cache==null){
            cache = new PerpetualCache("Localcache");

        }
        return cache;
    }

    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        return queryBuilder(sql,args,rowMapper);
    }

    public <T> List<T> query(String sql,RowMapper<T> rowMapper) throws DataAccessException {
        return queryBuilder(sql, null, rowMapper);
    }
    public List<Map<String, Object>> find(String sql,Object... parameters) throws JdbcDataException {
       return queryBuilder(sql, parameters, null);
    }

    public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
            throws DataAccessException {
        return selectOne(sql, rowMapper, null, argTypes, args);
    }


    public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException {
        return  selectOne(sql, null, requiredType, null, args);
    }


    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws DataAccessException {
        return selectOne(sql, null, requiredType, null, args);
    }

    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        return selectOne(sql, rowMapper, null, null, args);
    }


    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
        return selectOne(sql, rowMapper, null, null, args);
    }


    public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {
        return queryBuilder(sql, args, null);
    }



    public Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException {
        return selectOne(sql, null, null, null, args);
    }

    public int update(String sql, PreparedStatementSetter pss) throws DataAccessException {
        getCacheInstance().clear();
        getShowSQL(sql, null);
        return jdbcTemplate.update(sql, pss);
    }

    public int update(PreparedStatementCreator psc) throws DataAccessException {
        getCacheInstance().clear();
        return jdbcTemplate.update(psc);
    }
    public int update(String sql, Object... args) throws DataAccessException {
        getCacheInstance().clear();
        getShowSQL(sql, args);
        return jdbcTemplate.update(sql,args);
    }

    public int update(final PreparedStatementCreator psc, final KeyHolder generatedKeyHolder)
            throws DataAccessException {
        getCacheInstance().clear();
       return  jdbcTemplate.update(psc, generatedKeyHolder);
    }


    public int[] batchUpdate(String sql, final BatchPreparedStatementSetter pss) throws DataAccessException {
        getCacheInstance().clear();
        getShowSQL(sql, null);
        return jdbcTemplate.batchUpdate(sql, pss);
    }


    public Map<String, Object> call(CallableStatementCreator csc, List<SqlParameter> declaredParameters){
        return jdbcTemplate.call(csc, declaredParameters);
    }

    public <T> T execute(CallableStatementCreator csc, CallableStatementCallback<T> action){
        return jdbcTemplate.execute(csc, action);
    }

    public void execute(final String sql){
         getShowSQL(sql, null);
         jdbcTemplate.execute(sql);
    }

    private <T> List<T> queryBuilder(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        SqlCacheProvider sqlProvider=new DefaultCacheSqlProvider(sql,args);
        List<T>  result = new ArrayList<>();
       if(rowMapper!=null){
           result= (List<T>)jdbcTemplate.query(sql, args, new RowMapperResultSetExtractor<T>(rowMapper));
       }else{
           result= (List<T>) jdbcTemplate.queryForList(sql,args);
       }
        if(useLocalCache){
            CacheKey cacheKey =this.getCachekeyBuilder(sqlProvider).createCachekey();
            Object cacheObject = getCacheInstance().getOject(cacheKey);
            if (cacheObject == null) {
                getShowSQL(sql,args);
                List<T> object =result;
                getCacheInstance().putObject(cacheKey, object);
                return object;
            }else{
                return (List<T>) cacheObject;
            }

        }else{
            getShowSQL(sql,args);
            return result;
        }

    }


    private<T> T selectOne(String sql, RowMapper<T> rowMapper,Class<T> requiredType,  int[] argTypes,Object... args)  throws DataAccessException {

        T  result = null;
        if(rowMapper!=null){
              result= (T)jdbcTemplate.queryForObject(sql, args, rowMapper);
        }
        else if(requiredType!=null){
            result= (T)jdbcTemplate.queryForObject(sql, requiredType,args);
        }
        else if(argTypes!=null){
            result= (T)jdbcTemplate.queryForObject(sql,args,argTypes,rowMapper);
        } else{
            result= (T)jdbcTemplate.queryForMap(sql, args);
        }
        if(useLocalCache){
            SqlCacheProvider sqlProvider=new DefaultCacheSqlProvider(sql,args);
            CacheKey cacheKey =this.getCachekeyBuilder(sqlProvider).createCachekey();
            Object cacheObject = getCacheInstance().getOject(cacheKey);
            if (cacheObject != null) {
                return (T) cacheObject;
            } else {
                getShowSQL(sql,args);
                T object =result;
                getCacheInstance().putObject(cacheKey, object);
                return object;
            }
        }else{
            getShowSQL(sql,args);
            return result;
        }

    }

    public void clear(){
        getCacheInstance().clear();
    }

    private void getShowSQL(String sql, Object[] parameters){
        if(showSQL||logger.isDebugEnabled()){
           if(logger.isDebugEnabled()) {
                logger.debug("==> SQL:[ " + sql + " ]");
            }else {
               logger.info("==> SQL:[ " + sql + " ]");
            }

            getParameters(parameters);
        }

    }

    protected void getParameters(Object[] parameters){
        String  params="";
        if(parameters!=null&& parameters.length>0){
            for(Object parameter:parameters){
                params+=parameter+",";
            }
            params = params.substring(0, params.length()-1);
            if(logger.isDebugEnabled()) {
                logger.debug("==> parameters: " + params + " ");
            }else {
                logger.info("==> parameters: " + params + " ");
            }

        }

    }



}