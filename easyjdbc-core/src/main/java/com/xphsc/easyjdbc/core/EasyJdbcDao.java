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
import com.xphsc.easyjdbc.page.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${huipei.x}
 */

public interface EasyJdbcDao<T> {

    public EasyJdbcTemplate getEasyJdbcTemplate();

    /**
     *insert
     */
    public int insert(T persistent);

    /**
     * insertForKey and return primary key
     *persistent  Persistent Entities
     */
    public Object insertForKey(Object persistent);

    /**
     *batch insert
     */
    public int batchInsert(List<T> persistents);
    /**
     *Obtaining objects based on primary key values
     */
    public <T> T getByPrimaryKey(Serializable primaryKeyValue);
    /**
     *Delete objects based on primary key values
     */
    public int deleteByPrimaryKey(Serializable primaryKeyValue);
    /**
     *Delete objects based on primary key values for list
     */
    public int deleteByIds(Iterable primaryKeyValues);
    /**
     *update by object
     */
    public int update(T persistent);

    /**
     *update by object
     */
    public int updateWithNull(T persistent);

    /**
     *update by object for batchUpdate
     */
    public int batchUpdate(List<T> persistents);

    /**
     * ID exist
     */
    public boolean exists(Serializable primaryKeyValue);
    /**
     *Query all
     */
    public <T> List<T> findAll();
    /**
     *Query all by sorting object
     */
    public <T> List<T> findAll(Sorts sort);
    /**
     *Query all based on paging objects
     */
    public <T> PageInfo<T> findAll(PageInfo pageInfo);

    /**
     *All queries are based on paging and sorting objects
     */
    public <T> PageInfo<T> findAll(PageInfo pageInfo,Sorts sort);
    /**
     *  Query according to IDS
     */
    public <T> List<T> findByIds(Iterable values);
    /**
     *  count
     */
    public int count();
    /**
     *  query selector
     */
    public EasyJdbcSelector selector();

    public Example example();

    /**
     *Cache cleanup
     */
    public void cacheClear();
}

