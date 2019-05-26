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
import com.xphsc.easyjdbc.executor.UpdateExecutor;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.util.Assert;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by ${huipei.x}
 */
public  class SimpleJdbcDao<T>  implements EasyJdbcDao<T> {

    public Class<T> modelClass;

  public SimpleJdbcDao(EasyJdbcTemplate easyJdbcTemplate)  {
      this.easyJdbcTemplate=easyJdbcTemplate;
    }

    public SimpleJdbcDao()  {
        if (this.getClass().getGenericSuperclass() instanceof ParameterizedType){
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            modelClass = (Class<T>) pt.getActualTypeArguments()[0];
        }
    }

    private EasyJdbcTemplate easyJdbcTemplate;

    public void setEasyJdbcTemplate(EasyJdbcTemplate easyJdbcTemplate) {
        if(getEasyJdbcTemplate()!=null){
            this.easyJdbcTemplate = getEasyJdbcTemplate();
        }else{
            this.easyJdbcTemplate =easyJdbcTemplate;
        }
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
    public Object insertForKey(Object persistent) {
        return easyJdbcTemplate.insertKey(persistent);
    }

    @Override
    public int batchInsert(List<T> persistents) throws JdbcDataException{
        Assert.notEmpty(persistents, "Entity list cannot be empty");
        int rows = easyJdbcTemplate.batchInsert(persistents);
        return rows;
    }

    @Override
    public int deleteByPrimaryKey(Serializable primaryKeyValue) {
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        Assert.notNull(primaryKeyValue, "Primary key cannot be empty");
        int rows =easyJdbcTemplate.deleteByPrimaryKey(modelClass, primaryKeyValue);
        return rows;
    }


    @Override
    public int deleteByIds(Iterable primaryKeyValues){
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        int rows = easyJdbcTemplate.deleteByIds(modelClass, primaryKeyValues);
        return rows;
    }

    @Override
    public int update(T persistent) throws JdbcDataException{
        Assert.notNull(persistent, "Entities cannot be empty");
        int rows= easyJdbcTemplate.update(persistent);
        return rows;
    }

    @Override
    public int updateWithNull(T persistent) {
        UpdateExecutor executor = new UpdateExecutor(easyJdbcTemplate.getJdbcBuilder(),persistent,false);
        int rows = executor.execute();
        executor = null;
        return rows;
    }

    @Override
    public int batchUpdate(List<T> persistents) throws JdbcDataException{
        Assert.notEmpty(persistents, "Entity list cannot be empty");
        int rows=easyJdbcTemplate.batchUpdate(persistents);
        return rows;
    }

    @Override
    public boolean exists(Serializable primaryKeyValue) {
        return getByPrimaryKey(primaryKeyValue)!=null?true:false;
    }


    @Override
    public <T> T getByPrimaryKey(Serializable primaryKeyValue) {
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        Assert.notNull(primaryKeyValue, "Primary key cannot be empty");
        T entity=easyJdbcTemplate.getByPrimaryKey(modelClass, primaryKeyValue);
        return entity;
    }


    @Override
    public int count() {
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        Example example=example();
        return example.count();
    }


    @Override
    public <T> List<T> findAll() {
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        Example example=example();
        return example.list();
    }

    @Override
    public <T1> List<T1> findAll(Sorts sort) {
        Example example=example();
        example.orderByClause(sort);
        return example.list();
    }

    @Override
    public <T> PageInfo<T> findAll(PageInfo pageInfo){
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        Example example=example();
        example.pageInfo(pageInfo.pageNum,pageInfo.pageSize);
        return example.page();
    }

    @Override
    public <T> PageInfo<T> findAll(PageInfo pageInfo, Sorts sort) {
        Example example=example();
        example.orderByClause(sort);
        example.pageInfo(pageInfo.pageNum,pageInfo.pageSize);
        return example.page();
    }

    @Override
    public <T> List<T> findByIds(Iterable values){
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        List<T> list=easyJdbcTemplate.findByIds(modelClass, values);
        return  list;
    }
    @Override
    public EasyJdbcSelector selector() {
        return easyJdbcTemplate.selector();
    }

    @Override
    public Example example() {
        Assert.notNull(modelClass, "Entity interface generic type cannot be empty");
        return new Example(modelClass,easyJdbcTemplate.getJdbcBuilder(),getEasyJdbcTemplate().getDialectName());
    }

    @Override
    public void cacheClear() {
        easyJdbcTemplate.getJdbcBuilder().clear();
    }


}
