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
package com.xphsc.easyjdbc.core.processor;


import com.xphsc.easyjdbc.core.entity.Example;
import com.xphsc.easyjdbc.core.entity.Sorts;
import com.xphsc.easyjdbc.page.PageInfo;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author huipei.x
 * @date  2018-8-20
 * @description
 */
public class BaseMethodProcessor extends AbstractDaoMethodProcessor {

    private PageInfo pageInfo;

    @Override
    public Object process() {
        simpleJdbcDao.modelClass=persistentClass;
        MethodName methodName=new MethodName(method);
          if(this.parameters!=null){
              MethodParameterType methodParameterType=new MethodParameterType(method);
              if(methodParameterType.returnsPage){
                  if(methodName.returnFindAll){
                      pageInfo= (PageInfo) parameters[0];
                      Sorts sort=null;
                      if(parameters.length==2){
                          sort= (Sorts) parameters[1];
                      }
                      return sort==null?simpleJdbcDao.findAll(pageInfo):simpleJdbcDao.findAll(pageInfo,sort);
                  }
              }

              if(methodParameterType.returnsSorts){
                  if(methodName.returnFindAll){
                      Sorts sort= (Sorts) parameters[0];
                      return simpleJdbcDao.findAll(sort);
                  }
              }

              if(methodParameterType.returnsSerializable){
                  if(methodName.returnGetByPrimaryKey){
                      return simpleJdbcDao.getByPrimaryKey((Serializable) parameters[0]);
                  }
                  if(methodName.returnExists){
                      return simpleJdbcDao.exists((Serializable) parameters[0]);
                  }
                  if(methodName.returnGetById){
                      return simpleJdbcDao.getById((Serializable) parameters[0]);
                  }
                  if(methodName.returnDeleteByPrimaryKey){
                      return simpleJdbcDao.deleteByPrimaryKey((Serializable) parameters[0]);
                  }
              }
              if(methodParameterType.returnsObject){
                  if(methodName.returnInsert){
                      return simpleJdbcDao.insert(parameters[0]);

                  }else if(methodName.returnInsertForKey){
                      return simpleJdbcDao.insertForKey(parameters[0]);
                  }
                  else if(methodName.returnUpdate){
                      return simpleJdbcDao.update(parameters[0]);
                  }
                  else if(methodName.returnUpdateWithNull){
                      return simpleJdbcDao.updateWithNull(parameters[0]);
                  }
                  else if(methodName.returnDelete){
                      return simpleJdbcDao.delete(parameters[0]);
                  }
              }
              if(methodParameterType.returnsIterable) {
                  if(methodName.returnFindByIds){
                      return simpleJdbcDao.findByIds((Iterable) parameters[0]);
                  }
                  if(methodName.returnDeleteByIds){
                      return simpleJdbcDao.deleteByIds((Iterable) parameters[0]);
                  }
              }

              if(methodParameterType.returnsList) {
                  if(methodName.returnBatchInsert){
                      return simpleJdbcDao.batchInsert((List) parameters[0]);
                  }
                  if(methodName.returnBatchUpdate){
                      return simpleJdbcDao.batchUpdate((List) parameters[0]);
                  }
              }


          }else{
              if(methodName.returnFindAll){
                  return simpleJdbcDao.findAll();
              }
              if(methodName.returnCount) {
                  return simpleJdbcDao.count();
              }
              if(methodName.returnExample){
                  return simpleJdbcDao.example();
              }
              if(methodName.returnSelector){
                  return  simpleJdbcDao.selector();
              }
              if(methodName.returnGetEasyJdbcTemplate){
                  return  simpleJdbcDao.getEasyJdbcTemplate();
              }
              if(methodName.returnCacheClear){
                  simpleJdbcDao.cacheClear();
              }
          }

        return null;
    }

    public static class MethodParameterType {
        private boolean returnsSerializable;
        private boolean returnsObject;
        private boolean returnsPage;
        private boolean returnsList;
        private boolean returnsSorts;
        private boolean returnsIterable;


        public MethodParameterType(Method method) {
            Class<?> parameterType = method.getParameterTypes()[0];

            if (parameterType.isAssignableFrom(Sorts.class)) {
                this.returnsSorts = true;
            }
            if (parameterType.isAssignableFrom(Serializable.class)) {
                this.returnsSerializable = true;
            }
            if (parameterType.isAssignableFrom(PageInfo.class)) {
                this.returnsPage = true;
            }

            if (parameterType.isAssignableFrom(List.class)) {
                this.returnsList = true;
            }

            if (parameterType.isAssignableFrom(Object.class)) {
                this.returnsObject = true;
            }
            if (parameterType.isAssignableFrom(Iterable.class)) {
                this.returnsIterable = true;
            }


        }

    }


    public static class MethodName {
        private boolean returnFindAll;
        private boolean returnGetByPrimaryKey;
        private boolean returnExists;
        private boolean returnGetById;
        private boolean returnDeleteByPrimaryKey;
        private boolean  returnInsert;
        private boolean  returnInsertForKey;
        private boolean returnUpdate;
        private boolean returnUpdateWithNull;
        private boolean returnFindByIds;
        private boolean returnDeleteByIds;
        private boolean returnDelete;
        private boolean returnBatchInsert;
        private boolean returnBatchUpdate;
        private boolean returnCount;
        private boolean returnExample;
        private boolean returnGetEasyJdbcTemplate;
        private boolean returnCacheClear;
        private boolean returnSelector;
        public MethodName(Method method) {
            String  name=method.getName();
            if (name.equals(BaseMethodConstants.FIND_ALL)) {
                this.returnFindAll = true;
            }
            if (name.equals(BaseMethodConstants.EXISTS)) {
                this.returnExists = true;
            }
            if (name.equals(BaseMethodConstants.GET_BY_PRIMARY_KEY)) {
                this.returnGetByPrimaryKey = true;
            }

            if (name.equals(BaseMethodConstants.GET_BY_ID)) {
                this.returnGetById = true;
            }
            if (name.equals(BaseMethodConstants.DELETE_BY_PRIMARY_KEY)) {
                this.returnDeleteByPrimaryKey = true;
            }
            if (name.equals(BaseMethodConstants.INSERT)) {
                this.returnInsert = true;
            }
            if (name.equals(BaseMethodConstants.INSERT_FOR_KEY)) {
                this.returnInsertForKey = true;
            }

            if (name.equals(BaseMethodConstants.UPDATE)) {
                this.returnUpdate = true;
            }
            if (name.equals(BaseMethodConstants.UPDATE_WITH_NULL)) {
                this.returnUpdateWithNull = true;
            }
            if (name.equals(BaseMethodConstants.FIND_BY_IDS)) {
                this.returnFindByIds = true;
            }

            if (name.equals(BaseMethodConstants.DELETE_BY_IDS)) {
                this.returnDeleteByIds = true;
            }
            if (name.equals(BaseMethodConstants.DELETE)) {
                this.returnDelete = true;
            }
            if (name.equals(BaseMethodConstants.BATCH_INSERT)) {
                this.returnBatchInsert = true;
            }
            if (name.equals(BaseMethodConstants.BATCH_UPDATE)) {
                this.returnBatchUpdate = true;
            }
            if (name.equals(BaseMethodConstants.COUNT)) {
                this.returnCount = true;
            }
            if (name.equals(BaseMethodConstants.EXAMPLE)) {
                this.returnExample = true;
            }
            if (name.equals(BaseMethodConstants.SELECTOR)) {
                this.returnSelector = true;
            }
            if (name.equals(BaseMethodConstants.GET_EASY_JDBC_TEMPLATE)) {
                this.returnGetEasyJdbcTemplate = true;
            }
            if (name.equals(BaseMethodConstants.CLEAR)) {
                this.returnCacheClear = true;
            }


        }

    }


        private class BaseMethodConstants{
        public static final String FIND_ALL="findAll";
        public static final String GET_BY_PRIMARY_KEY="getByPrimaryKey";
        public static final String EXISTS="exists";
        public static final String GET_BY_ID="getById";
        public static final String DELETE_BY_PRIMARY_KEY="deleteByPrimaryKey";
        public static final String INSERT="insert";
        public static final String INSERT_FOR_KEY="insertForKey";
        public static final String UPDATE="update";
        public static final String UPDATE_WITH_NULL="updateWithNull";
        public static final String  FIND_BY_IDS="findByIds";
        public static final String  DELETE_BY_IDS="deleteByIds";
        public static final String  DELETE="delete";
        public static final String  BATCH_INSERT="batchInsert";
        public static final String  BATCH_UPDATE="batchUpdate";
        public static final String  COUNT="count";
        public static final String  EXAMPLE="example";
        public static final String  SELECTOR="selector";
        public static final String  CLEAR="cacheClear";
        public static final String  GET_EASY_JDBC_TEMPLATE="getEasyJdbcTemplate";

    }
}
