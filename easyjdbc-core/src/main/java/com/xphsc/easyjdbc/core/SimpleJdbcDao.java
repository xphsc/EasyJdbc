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
package com.xphsc.easyjdbc.core;


import com.xphsc.easyjdbc.EasyJdbcSelector;
import com.xphsc.easyjdbc.EasyJdbcTemplate;
import com.xphsc.easyjdbc.core.entity.Example;
import com.xphsc.easyjdbc.core.entity.Sorts;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.lambda.LambdaSupplier;
import com.xphsc.easyjdbc.core.support.JdbcBuilder;
import com.xphsc.easyjdbc.executor.DeleteExecutor;
import com.xphsc.easyjdbc.executor.UpdateExecutor;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.util.Assert;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description:  Interface for data access operations on persistent entities using JDBC.
 * Provides methods for basic CRUD operations, batch processing, and query functionality.
 * @param <T> The type of the persistent entity.
 */
public  class SimpleJdbcDao<T>  implements EasyJdbcDao<T> {

    public Class<T> modelClass;
    private JdbcBuilder jdbcBuilder;
    private String dialectName;

    public SimpleJdbcDao(EasyJdbcTemplate easyJdbcTemplate) {
        this.easyJdbcTemplate = easyJdbcTemplate;
    }

    public SimpleJdbcDao() {
        if (this.getClass().getGenericSuperclass() instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            modelClass = (Class<T>) pt.getActualTypeArguments()[0];
        }
    }

    private EasyJdbcTemplate easyJdbcTemplate;


    public void easyJdbcTemplate(LambdaSupplier<EasyJdbcTemplate> easyJdbcTemplate) {
        if (getEasyJdbcTemplate() != null) {
            this.easyJdbcTemplate = getEasyJdbcTemplate();
        } else {
            this.easyJdbcTemplate = easyJdbcTemplate.get();
        }
        jdbcBuilder = this.easyJdbcTemplate.getJdbcBuilder();
        dialectName = this.easyJdbcTemplate.getDialectName();

    }

    @Override
    public EasyJdbcTemplate getEasyJdbcTemplate() {
        return easyJdbcTemplate;
    }

    @Override
    public int insert(T persistent) throws JdbcDataException {
        Assert.notNull(persistent, "Entity cannot be empty");
        int rows = easyJdbcTemplate.insert(persistent);
        return rows;
    }

    @Override
    public Object insertForKey(T persistent) {
        return easyJdbcTemplate.insertKey(persistent);
    }

    @Override
    public int batchInsert(List<T> persistents) throws JdbcDataException {
        Assert.notEmpty(persistents, "Entity list cannot be empty");
        int rows = easyJdbcTemplate.batchInsert(persistents);
        return rows;
    }

    @Override
    public int deleteByPrimaryKey(Serializable primaryKeyValue) {
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        Assert.notNull(primaryKeyValue, "Primary key cannot be empty");
        int rows = easyJdbcTemplate.deleteByPrimaryKey(modelClass, primaryKeyValue);
        return rows;
    }


    @Override
    public int deleteByIds(Iterable primaryKeyValues) {
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        int rows = easyJdbcTemplate.deleteByIds(modelClass, primaryKeyValues);
        return rows;
    }

    @Override
    public int delete(T persistent) {
        DeleteExecutor executor = new DeleteExecutor(this::getJdbcBuilder, persistent);
        int rows = executor.execute();
        executor = null;
        return rows;
    }

    @Override
    public int update(T persistent) throws JdbcDataException {
        Assert.notNull(persistent, "Entities cannot be empty");
        int rows = easyJdbcTemplate.update(persistent);
        return rows;
    }

    @Override
    public int updateWithNull(T persistent) {
        UpdateExecutor executor = new UpdateExecutor(this::getJdbcBuilder, persistent, false);
        int rows = executor.execute();
        executor = null;
        return rows;
    }

    @Override
    public int batchUpdate(List<T> persistents) throws JdbcDataException {
        Assert.notEmpty(persistents, "Entity list cannot be empty");
        int rows = easyJdbcTemplate.batchUpdate(persistents);
        return rows;
    }

    @Override
    public boolean exists(Serializable primaryKeyValue) {
        return getByPrimaryKey(primaryKeyValue) != null ? true : false;
    }


    @Override
    public <T> T getByPrimaryKey(Serializable primaryKeyValue) {
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        Assert.notNull(primaryKeyValue, "Primary key cannot be empty");
        T entity = easyJdbcTemplate.getByPrimaryKey(modelClass, primaryKeyValue);
        return entity;
    }

    @Override
    public Optional<T> getById(Serializable id) {
        return Optional.ofNullable(getByPrimaryKey(id));
    }


    @Override
    public long count() {
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        Example example = example();
        return example.count();
    }


    @Override
    public <T> List<T> findAll() {
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        Example example = example();
        return example.list();
    }

    @Override
    public <T> List<T> findAll(Sorts sort) {
        Example example = example();
        example.orderByClause(sort);
        return example.list();
    }

    @Override
    public <T> PageInfo<T> findAll(PageInfo pageInfo) {
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        Example example = example();
        if (pageInfo.getOffset() == -1 && pageInfo.getPageNum() >= 1) {
            example.pageInfo(pageInfo.getPageNum(), pageInfo.getPageSize());
        } else {
            example.offsetPage(pageInfo.getOffset() >= 0 ? pageInfo.getOffset() : 0, pageInfo.getLimit());
        }
        return example.page();
    }

    @Override
    public <T> PageInfo<T> findAll(PageInfo pageInfo, Sorts sort) {
        Example example = example();
        example.orderByClause(sort);
        if (pageInfo.getOffset() == -1 && pageInfo.getPageNum() >= 1) {
            example.pageInfo(pageInfo.getPageNum(), pageInfo.getPageSize());
        } else {
            example.offsetPage(pageInfo.getOffset() >= 0 ? pageInfo.getOffset() : 0, pageInfo.getLimit());
        }
        return example.page();
    }

    @Override
    public <T> List<T> findByIds(Iterable values) {
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        List<T> list = easyJdbcTemplate.findByIds(modelClass, values);
        return list;
    }

    @Override
    public EasyJdbcSelector selector() {
        return easyJdbcTemplate.selector();
    }

    @Override
    public Example example() {
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        return easyJdbcTemplate.example(modelClass);
    }

    @Override
    public void cacheClear() {
        easyJdbcTemplate.clear();
    }

    private JdbcBuilder getJdbcBuilder() {
        return jdbcBuilder;
    }

    private String getDialectName() {
        return dialectName;
    }
}
