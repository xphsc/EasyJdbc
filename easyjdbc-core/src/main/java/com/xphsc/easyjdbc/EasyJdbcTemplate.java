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
package com.xphsc.easyjdbc;




import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.entity.Example;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.executor.*;
import com.xphsc.easyjdbc.executor.ids.DeleteByIdsExecutor;
import com.xphsc.easyjdbc.executor.ids.FindByIdsExecutor;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.page.PageInfoImpl;
import com.xphsc.easyjdbc.core.support.EasyJdbcAccessor;
import com.xphsc.easyjdbc.util.Assert;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by ${huipei.x}
 */
public class EasyJdbcTemplate extends EasyJdbcAccessor {


     public EasyJdbcTemplate() {
          this.setJdbcTemplate(new JdbcTemplate());
     }


    private  EasyJdbcTemplate(Builder builder){
        this.setJdbcTemplate(builder.jdbcTemplate!=null?builder.jdbcTemplate:new JdbcTemplate());
        this.setDataSource(builder.dataSource);
        this.setDialectName(builder.dialectName);
        this.useLocalCache(builder.useLocalCache);
        this.showSQL(builder.showSQL);
        this.afterPropertiesSet();

    }

    public static Builder builder(){
        return  new Builder();
    }

    public static class Builder {
        private JdbcTemplate jdbcTemplate;
        private DataSource dataSource;
        private  String dialectName;
        private boolean useLocalCache;
        private boolean showSQL;

        public Builder jdbcTemplate(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
            return this;
        }

        public Builder dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Builder dialectName(String dialectName) {
            this.dialectName = dialectName;
            return this;
        }

        public Builder useLocalCache(boolean useLocalCache) {
            this.useLocalCache = useLocalCache;
            return this;
        }

        public Builder showSQL(boolean showSQL) {
            this.showSQL = showSQL;
             return this;
        }

        public Builder() {
        }
        public EasyJdbcTemplate build() {
            return new EasyJdbcTemplate(this);
        }
    }
    /**
     * insert
     * persistent Persistent Entities
     * Number of rows affected by changes
     */
    public int insert(Object persistent) throws JdbcDataException {
        Assert.notNull(persistent, "Entities cannot be empty");
        InsertExecutor executor = new InsertExecutor(getJdbcBuilder(), persistent);
        int rows = (int) executor.execute();
        executor = null;
        return rows;
    }

    /**
     * insert and return primary key
     *persistent  Persistent Entities
     */
    public Object insertKey(Object persistent) throws JdbcDataException {
        Assert.notNull(persistent, "Entities cannot be empty");
        InsertExecutor executor = new InsertExecutor(getJdbcBuilder(), persistent,true);
        Object rows = (Object) executor.execute();
        executor = null;
        return rows;
    }

    /**
     * Batch insertion
     * If the amount of data is too large, it is recommended to insert it in stages, preferably not more than 10,000 at a time.
     * @return Number of rows inserted
     */
    public int batchInsert(List<?> persistents) throws JdbcDataException{
        Assert.notEmpty(persistents, "Entity list cannot be empty");
        BatchInsertExecutor executor = new BatchInsertExecutor(getJdbcBuilder(),persistents);
        int[] rows = executor.execute();
        executor = null;
        return rows.length;
    }

    /**
     *  insert
     * insertSql constructor insertion
     */
    public int insert(SQL insertSql,Object... parameters) throws JdbcDataException{
        Assert.hasText(insertSql.toString(), "The SQL constructor cannot be empty");
        int rows =getJdbcBuilder().update(insertSql.toString(), parameters);
        return rows;
    }

    /**
     * delete
     * @param persistentClass Entity class
     * @param primaryKeyValue primary key
     */
    public int deleteByPrimaryKey(Class<?> persistentClass,Serializable primaryKeyValue) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        Assert.notNull(primaryKeyValue, "Primary key cannot be empty");
        Assert.hasText(primaryKeyValue.toString(), "Primary key cannot be empty");
        DeleteExecutor executor = new DeleteExecutor(getJdbcBuilder(),persistentClass,primaryKeyValue);
        int rows = executor.execute();
        executor = null;
        return rows;
    }

    /**
     * delete
     * @param persistentClass Entity class
     * @param primaryKeyValues primary key
     */
    public int deleteByIds(Class<?> persistentClass,Iterable primaryKeyValues) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        Assert.notNull(primaryKeyValues, "Primary key cannot be empty");
        Assert.hasText(primaryKeyValues.toString(), "Primary key cannot be empty");
        DeleteByIdsExecutor executor = new DeleteByIdsExecutor(this.getJdbcBuilder(),persistentClass,primaryKeyValues);
        int rows = executor.execute();
        executor = null;
        return rows;
    }


    /**
     * SQL delete
     * @param deleteSql SQL constructor
     * @param parameters
     */
    public int delete(SQL deleteSql,Object... parameters) throws JdbcDataException{
        Assert.hasText(deleteSql.toString(), "The SQL constructor cannot be empty");
        int rows = this.getJdbcBuilder().update(deleteSql.toString(), parameters);
        return rows;
    }



    /**
     * update
     * @param persistent Persistent Entity List
     * @return  Number of rows affected
     */
    public int update(Object persistent) throws JdbcDataException{
        Assert.notNull(persistent, "Entities cannot be empty");
        UpdateExecutor executor = new UpdateExecutor(getJdbcBuilder(),persistent,true);
        int rows = executor.execute();
        executor = null;
        return rows;
    }
    /**
     * Batch update
     * @param ? Persistent Entity List
     * @return Number of rows affected
     */
    public int batchUpdate(List<?> persistents) throws JdbcDataException{
        Assert.notEmpty(persistents, "Entity list cannot be emp");
        BatchUpdateExecutor executor = new BatchUpdateExecutor(getJdbcBuilder(),persistents);
        int[] rows = executor.execute();
        executor = null;//hlep gc.
        return rows.length;
    }

    /**
     * SQL update
     * @param updateSql SQL constructor
     * @param parameters parameters
     */
    public int update(SQL updateSql,Object... parameters) throws JdbcDataException{
        Assert.notNull(updateSql, "The SQL constructor cannot be empty");
        int rows = this.getJdbcBuilder().update(updateSql.toString(), parameters);
        return rows;
    }

    /**
     * Get object values
     * @param ? Persistent Entity Class
     * @param primaryKeyValue primary key
     * @return
     */
    public <T> T getByPrimaryKey(Class<?> persistentClass,Serializable primaryKeyValue) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        Assert.notNull(primaryKeyValue, "Primary key cannot be empty");
        GetExecutor<T> executor = new GetExecutor<T>(getJdbcBuilder(),persistentClass,primaryKeyValue);
        try{
            T entity = executor.execute();
            return entity;
        } catch(EmptyResultDataAccessException e){
            //When the query result is empty, it will be thrown：EmptyResultDataAccessException，这里规避这个异常直接返回null
        }
        executor = null;//hlep gc.
        return null;

    }

    /**
     * @param sql SQL statement
     * @param persistentClass Entity type
     * @param parameters parameters
     * @return
     */
    public <T> T get(String sql,Class<?> persistentClass,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        List<T> results = find( sql,persistentClass,parameters);
        int size = (results != null ? results.size() : 0);
        if(size>1){
            new IncorrectResultSizeDataAccessException(1, size);
        }
        Assert.notEmpty(results,"The get method must not be empty.");
        return results.get(0);
    }

    /**
     *  Get all records
     * @param persistentClass
     */
    public <T> List<T> findAll(Class<?> persistentClass) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        Example example= example(persistentClass);
        return example.list();
    }

    /**
     *  Get all records
     * @param persistentClass
     */
    public <T> List<T> findAll(Class<?> persistentClass,PageInfo page) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        Example example= example(persistentClass);
        if(page.getOffset()==-1){
            Assert.isTrue(page.getPageNum()>=1, " PageNum must be greater than or equal to 1");
            Assert.isTrue(page.getPageSize() > 0, "PageSize must be greater than 0");
            example.pageInfo(page.getPageNum(), page.getPageSize());
        }else{
            Assert.isTrue(page.getOffset()>=0, " 0ffset must be greater than or equal to 0");
            Assert.isTrue(page.getLimit() > 0, "Limit must be greater than 0");
            example.offsetPage(page.getOffset(), page.getLimit());
        }

        return example.list();
    }


    /**
     * Entity query
     * @param selectSql Query SQL constructor
     * @param persistentClass Persistent Entity Class
     * @param parameters Query parameters
     */
    public <T> List<T> find(SQL selectSql,Class<?> persistentClass,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        Assert.hasText(selectSql.toString(), "SQL statement cannot be empty");
        FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(getJdbcBuilder(),this.getDialectName(),persistentClass,selectSql.toString(),parameters);
        List<T> list = executor.execute();
        executor = null;
        return list;
    }

    /**
     * Entity query
     * @param selectSql Query SQL constructor
     * @param offset Start line
     * @param limit Number of bars
     * @param persistentClass Persistent Entity Class
     * @param parameters Query parameters
     */
    public <T> List<T> find(SQL selectSql,Class<?> persistentClass,Integer offset,Integer limit,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        Assert.hasText(selectSql.toString(), "SQL statement cannot be empty");
        Assert.isTrue(offset >= 0, "0ffset must be greater than or equal to 0");
        Assert.isTrue(limit > 0, "Limit must be greater than 0");
        FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(getJdbcBuilder(),this.getDialectName(),persistentClass,selectSql.toString(),parameters,null,offset,limit);
        List<T> list = executor.execute();
        executor = null;
        return list;
    }

    /**
     * Entity query
     * @param selectSql Query SQL constructor
     * @param page object
     * @param persistentClass Persistent Entity Class
     * @param parameters Query parameters
     */
    public <T> List<T> find(SQL selectSql,Class<?> persistentClass,PageInfo page,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        Assert.hasText(selectSql.toString(), "SQL statement cannot be empty");
        FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(getJdbcBuilder(),this.getDialectName(),persistentClass,selectSql.toString(),parameters,null,page);
        List<T> list = executor.execute();
        executor = null;
        return list;
    }

    /**
     * Entity query
     * @param selectSql Query SQL constructor
     * @param page object
     * @param persistentClass Persistent Entity Class
     * @param parameters Query parameters
     */
    public <T> PageInfo<T> findByPage(SQL selectSql,Class<?> persistentClass,PageInfo page,Object... parameters) throws JdbcDataException{
        List<T> list= find(selectSql,persistentClass,page,parameters);
        int total= count(selectSql.toString(), parameters);
        return new PageInfoImpl<T>(list,total,page.getPageNum(),page.getPageSize());
    }


    /**
     * Entity query
     * @param sql Query SQL
     * @param persistentClass Persistent Entity Class
     */
    public <T> List<T> find(String sql,Class<?> persistentClass,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        Assert.hasText(sql, "SQL statement cannot be empty");
        FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(getJdbcBuilder(),this.getDialectName(),persistentClass,sql,parameters);
        List<T> list = executor.execute();
        executor = null;
        return list;
    }

    public List<Map<String, Object>> find(String sql,Object... parameters) throws JdbcDataException{
        Assert.hasText(sql, "SQL statement cannot be empty");
        List<Map<String, Object>> list=this.getJdbcBuilder().queryForList(sql, parameters);
        return list;
    }



    /**
     * Entity query
     * @param sql Query SQL
     * @param persistentClass Persistent Entity Class
     */
    public <T> List<T> find(String sql,Class<?> persistentClass,PageInfo page,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        Assert.hasText(sql, "SQL statement cannot be empty");
        FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(getJdbcBuilder(),this.getDialectName(),persistentClass,sql,parameters,null,page);
        List<T> list = executor.execute();
        executor = null;
        return list;
    }

    public <T> PageInfo<T> findByPage(String selectSql,Class<?> persistentClass,PageInfo page,Object... parameters) throws JdbcDataException{
        List<T> list= find(selectSql,persistentClass,page,parameters);
        int total= count(selectSql.toString(), parameters);
        return new PageInfoImpl<T>(list,total,page.getPageNum(),page.getPageSize());
    }

    /**
     * Entity query
     * @param sql Query SQL
     * @param offset Start line
     * @param limit Number of bars
     * @param persistentClass Persistent Entity Class
     */
    public <T> List<T> find(String sql,Class<?> persistentClass,Integer offset,Integer limit,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        Assert.hasText(sql, "SQL statement cannot be empty");
        Assert.isTrue(offset>=0, "Offset must be greater than or equal to 0");
        Assert.isTrue(limit > 0, "Limit must be greater than 0");
        FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(getJdbcBuilder(),this.getDialectName(),persistentClass,sql,parameters,null,offset,limit);
        List<T> list = executor.execute();
        executor = null;
        return list;
    }

    public <T> PageInfo<T> findByPage(String selectSql,Class<?> persistentClass,Integer offset,Integer limit,Object... parameters) throws JdbcDataException{
        List<T> list= find(selectSql,persistentClass,offset,limit,parameters);
        int total= count(selectSql.toString(), parameters);
        int pageNum=(int) Math.ceil((double) ((offset +limit) / limit));
        int pageSize=limit;
        return new PageInfoImpl<T>(list,total,pageNum,pageSize);
    }


    public <T> List<T> findByIds(Class<?> persistentClass,Iterable values) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        Assert.notNull(values, "Ids cannot be empty");
        FindByIdsExecutor<List<T>> executor= new FindByIdsExecutor(this.getJdbcBuilder(),persistentClass,values);
        List<T> list = executor.execute();
        executor = null;
        return list;
    }
    /**
     * Number statistics
     * @param sql Statistics SQL
     * @param parameters Statistical parameter
     */
    public int count(String sql,Object... parameters) throws JdbcDataException{
        Assert.hasText(sql, "SQL statement cannot be empty");
        CountExecutor executor =  new CountExecutor(getJdbcBuilder(),sql,parameters);
        int count = executor.execute();
        executor = null;
        return count;
    }

    /**
     *
     * @param persistentClass object
     * @return
     * @throws JdbcDataException
     */
    public int count(Class<?> persistentClass) throws JdbcDataException{
        Assert.notNull(persistentClass, "Entity type cannot be empty");
        CountExecutor executor =  new CountExecutor(getJdbcBuilder(),persistentClass);
        int count = executor.execute();
        executor = null;
        return count;
    }


    public Map<?,?> call(String sql, Class<?> persistentClass, Map<Integer, Integer> outParameters,  Object[] parameters) throws JdbcDataException{
        ExecProcExecutor executor =  new ExecProcExecutor(getJdbcBuilder(),sql,persistentClass,outParameters,parameters);
        Map<?,?> count = (Map<?, ?>) executor.execute();
        executor = null;
        return count;
    }



    /**
     *  @since 1.0.7 version, sub-attributes are out of date
     * @param example
     */
    @Deprecated
   public <T> List<T>  findByExample(Example example) throws JdbcDataException{
      example.jdbcTemplate=this.getJdbcBuilder();
       example.dialectName=getDialectName();
       List<T> list=example.list();
        return list;
    }

    /**
     *  @since 1.0.7 version, sub-attributes are out of date
     * @param example
     */
    @Deprecated
    public <T> T  getByExample(Example example) throws JdbcDataException {
        List<T> results = findByExample(example);
        int size = (results != null ? results.size() : 0);
        if(size>1){
            new IncorrectResultSizeDataAccessException(1, size);
        }
        return results.get(0);
    }

    /**
     *  @since 1.0.7 version, sub-attributes are out of date
     * @param example
     */
    @Deprecated
    public int countByExample(Example example) throws JdbcDataException{
       example.jdbcTemplate=this.getJdbcBuilder();
       example.dialectName=getDialectName();
        int count=example.count();
        return count;
    }



    /**
     *  @since 1.0.7 version, sub-attributes are out of date
     * @param example
     */
    @Deprecated
   public <T> PageInfo<T> findByPage(Example example) throws JdbcDataException{
       example.jdbcTemplate=this.getJdbcBuilder();
       example.dialectName=getDialectName();
       PageInfo<T> pageInfo=example.page();
        return pageInfo;
    }

    public void execute(final String sql){
        getJdbcBuilder().execute(sql);
    }


    /**
     * query selector
     */
    public EasyJdbcSelector selector(){
        return new EasyJdbcSelector(getJdbcBuilder(),this.getDialectName());
    }


    public Example example(Class<?> persistentClass){
       return new Example(persistentClass,getJdbcBuilder(),this.getDialectName());

    }

    /**
     * Clear cache
     */
    public void clear(){
        this.getJdbcBuilder().clear();
    }

}
