/*
 * Copyright (c) 2019 huipei.x
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
import com.xphsc.easyjdbc.page.PageInfo;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: EasyJdbcOperations 定义了一个简单的JDBC操作接口，用于执行基本的数据库操作
 */
public interface EasyJdbcOperations {

    /**
     * insert
     * persistent Persistent Entities
     * Number of rows affected by changes
     */
    int insert(Object persistent) throws JdbcDataException;

    int insertWithNull(Object persistent);

    /**
     * insert and return primary key
     * persistent  Persistent Entities
     */
    Object insertKey(Object persistent) throws JdbcDataException;

    /**
     * Batch insertion
     * If the amount of data is too large, it is recommended to insert it in stages, preferably not more than 10,000 at a time.
     *
     * @return Number of rows inserted
     */
    int batchInsert(List<?> persistents) throws JdbcDataException;

    /**
     * insert
     * insertSql constructor insertion
     */
    int insert(SQL insertSql, Object... parameters) throws JdbcDataException;

    /**
     * delete
     *
     * @param persistentClass Entity class
     * @param primaryKeyValue primary key
     */
    int deleteByPrimaryKey(Class<?> persistentClass, Serializable primaryKeyValue) throws JdbcDataException;

    /**
     * delete
     *
     * @param persistentClass  Entity class
     * @param primaryKeyValues primary key
     */
    int deleteByIds(Class<?> persistentClass, Iterable primaryKeyValues) throws JdbcDataException;


    /**
     * SQL delete
     *
     * @param deleteSql  SQL constructor
     * @param parameters
     */
    int delete(SQL deleteSql, Object... parameters) throws JdbcDataException;

    /**
     * update
     *
     * @param persistent Persistent Entity List
     * @return Number of rows affected
     */
    int update(Object persistent) throws JdbcDataException;

    /**
     * Batch update
     *
     * @param ? Persistent Entity List
     * @return Number of rows affected
     */
    int batchUpdate(List<?> persistents) throws JdbcDataException;

    /**
     * SQL update
     *
     * @param updateSql  SQL constructor
     * @param parameters parameters
     */
    int update(SQL updateSql, Object... parameters) throws JdbcDataException;

    /**
     * Get object values
     *
     * @param ?               Persistent Entity Class
     * @param primaryKeyValue primary key
     * @return
     */
    <T> T getByPrimaryKey(Class<?> persistentClass, Serializable primaryKeyValue) throws JdbcDataException;

    /**
     * @param sql             SQL statement
     * @param persistentClass Entity type
     * @param parameters      parameters
     * @return
     */
    <T> T get(String sql, Class<?> persistentClass, Object... parameters) throws JdbcDataException;

    /**
     * Get all records
     *
     * @param persistentClass
     */
    <T> List<T> findAll(Class<?> persistentClass) throws JdbcDataException;

    /**
     * Get all records
     *
     * @param persistentClass
     */
    <T> List<T> findAll(Class<?> persistentClass, PageInfo page) throws JdbcDataException;

    /**
     * Entity query
     *
     * @param selectSql       Query SQL constructor
     * @param persistentClass Persistent Entity Class
     * @param parameters      Query parameters
     */
    <T> List<T> find(SQL selectSql, Class<?> persistentClass, Object... parameters) throws JdbcDataException;

    /**
     * Entity query
     *
     * @param selectSql       Query SQL constructor
     * @param offset          Start line
     * @param limit           Number of bars
     * @param persistentClass Persistent Entity Class
     * @param parameters      Query parameters
     */
    <T> List<T> find(SQL selectSql, Class<?> persistentClass, Integer offset, Integer limit, Object... parameters);

    /**
     * Entity query
     *
     * @param selectSql       Query SQL constructor
     * @param page            object
     * @param persistentClass Persistent Entity Class
     * @param parameters      Query parameters
     */
    <T> List<T> find(SQL selectSql, Class<?> persistentClass, PageInfo page, Object... parameters) throws JdbcDataException;

    /**
     * Entity query
     *
     * @param selectSql       Query SQL constructor
     * @param page            object
     * @param persistentClass Persistent Entity Class
     * @param parameters      Query parameters
     */
    <T> PageInfo<T> findByPage(SQL selectSql, Class<?> persistentClass, PageInfo page, Object... parameters) throws JdbcDataException;

    /**
     * Entity query
     *
     * @param sql             Query SQL
     * @param persistentClass Persistent Entity Class
     */
    <T> List<T> find(String sql, Class<?> persistentClass, Object... parameters) throws JdbcDataException;


    List<Map<String, Object>> find(String sql, Object... parameters) throws JdbcDataException;


    /**
     * Entity query
     *
     * @param sql             Query SQL
     * @param persistentClass Persistent Entity Class
     */
    <T> List<T> find(String sql, Class<?> persistentClass, PageInfo page, Object... parameters) throws JdbcDataException;


    <T> PageInfo<T> findByPage(String selectSql, Class<?> persistentClass, PageInfo page, Object... parameters) throws JdbcDataException;

    /**
     * Entity query
     *
     * @param sql             Query SQL
     * @param offset          Start line
     * @param limit           Number of bars
     * @param persistentClass Persistent Entity Class
     */
    <T> List<T> find(String sql, Class<?> persistentClass, Integer offset, Integer limit, Object... parameters);


    public <T> PageInfo<T> findByPage(String selectSql, Class<?> persistentClass, Integer offset, Integer limit, Object... parameters);


    <T> List<T> findByIds(Class<?> persistentClass, Iterable values);

    /**
     * Number statistics
     *
     * @param sql        Statistics SQL
     * @param parameters Statistical parameter
     */
    long count(String sql, Object... parameters) throws JdbcDataException;

    /**
     * @param persistentClass object
     * @return
     * @throws JdbcDataException
     */
    long count(Class<?> persistentClass) throws JdbcDataException;


    Map<?, ?> call(String sql, Class<?> persistentClass, Map<Integer, Integer> outParameters, Object[] parameters) throws JdbcDataException;


    void execute(final String sql);


    /**
     * query selector
     */
    EasyJdbcSelector selector();


    Example example(Class<?> persistentClass);

    /**
     * Clear cache
     */
    void clear();
}
