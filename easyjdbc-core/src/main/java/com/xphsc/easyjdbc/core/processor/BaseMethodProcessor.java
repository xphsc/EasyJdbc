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
          if(this.parameters!=null){
              MethodParameterType methodParameterType=new MethodParameterType(method);
              if(methodParameterType.returnsPage){
                  if(method.getName().equals(BaseMethodConstants.FIND_ALL)){
                      pageInfo= (PageInfo) parameters[0];
                      Sorts sort=null;
                      if(parameters.length==2){
                          sort= (Sorts) parameters[1];
                      }
                      return sort==null?simpleJdbcDao.findAll(pageInfo):simpleJdbcDao.findAll(pageInfo,sort);
                  }
              }

              if(methodParameterType.returnsSorts){
                  if(method.getName().equals(BaseMethodConstants.FIND_ALL)){
                      Sorts sort= (Sorts) parameters[0];
                      return simpleJdbcDao.findAll(sort);
                  }
              }

              if(methodParameterType.returnsSerializable){
                  if(method.getName().equals(BaseMethodConstants.GET_BY_PRIMARY_KEY)){
                      return simpleJdbcDao.getByPrimaryKey((Serializable) parameters[0]);
                  }

                  if(method.getName().equals(BaseMethodConstants.EXISTS)){
                      return simpleJdbcDao.exists((Serializable) parameters[0]);
                  }

                  if(method.getName().equals(BaseMethodConstants.DELETE_BY_PRIMARY_KEY)){
                      return simpleJdbcDao.deleteByPrimaryKey((Serializable) parameters[0]);
                  }
              }

              if(methodParameterType.returnsObject){
                  if(method.getName().equals(BaseMethodConstants.INSERT)){
                      return simpleJdbcDao.insert(parameters[0]);

                  }else if(method.getName().equals(BaseMethodConstants.INSERT_FOR_KEY)){
                      return simpleJdbcDao.insertForKey(parameters[0]);
                  }
                  else if(method.getName().equals(BaseMethodConstants.UPDATE)){
                      return simpleJdbcDao.update(parameters[0]);
                  }
                  else if(method.getName().equals(BaseMethodConstants.UPDATE_WITH_NULL)){
                      return simpleJdbcDao.updateWithNull(parameters[0]);
                  }


              }
              if(methodParameterType.returnsIterable) {
                  if(method.getName().equals(BaseMethodConstants.FIND_BY_IDS)){
                      return simpleJdbcDao.findByIds((Iterable) parameters[0]);
                  }
                  if(method.getName().equals(BaseMethodConstants.DELETE_BY_IDS)){
                      return simpleJdbcDao.deleteByIds((Iterable) parameters[0]);
                  }
              }

              if(methodParameterType.returnsList) {
                  if(method.getName().equals(BaseMethodConstants.BATCH_INSERT)){
                      return simpleJdbcDao.batchInsert((List) parameters[0]);
                  }
                  if(method.getName().equals(BaseMethodConstants.BATCH_UPDATE)){
                      return simpleJdbcDao.batchUpdate((List) parameters[0]);
                  }
              }


          }else{
              if(method.getName().equals(BaseMethodConstants.FIND_ALL)){
                  return simpleJdbcDao.findAll();
              }
              if(method.getName().equals(BaseMethodConstants.COUNT)) {
                  return simpleJdbcDao.count();
              }
              if(method.getName().equals(BaseMethodConstants.EXAMPLE)){
                  return simpleJdbcDao.example();
              }
              if(method.getName().equals(BaseMethodConstants.SELECTOR)){
                  return  simpleJdbcDao.selector();
              }
              if(method.getName().equals(BaseMethodConstants.GET_EASY_JDBC_TEMPLATE)){
                  return  simpleJdbcDao.getEasyJdbcTemplate();
              }
              if(method.getName().equals(BaseMethodConstants.CLEAR)){
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
        private class BaseMethodConstants{
        public static final String FIND_ALL="findAll";
        public static final String GET_BY_PRIMARY_KEY="getByPrimaryKey";
        public static final String EXISTS="exists";
        public static final String DELETE_BY_PRIMARY_KEY="deleteByPrimaryKey";
        public static final String INSERT="insert";
        public static final String INSERT_FOR_KEY="insertForKey";
        public static final String UPDATE="update";
        public static final String UPDATE_WITH_NULL="updateWithNull";
        public static final String  FIND_BY_IDS="findByIds";
        public static final String  DELETE_BY_IDS="deleteByIds";
        public static final String  BATCH_INSERT="batchInsert";
        public static final String  BATCH_UPDATE="batchUpdate";
        public static final String  COUNT="count";
        public static final String  EXAMPLE="example";
        public static final String  SELECTOR="selector";
        public static final String  CLEAR="cacheClear";
        public static final String  GET_EASY_JDBC_TEMPLATE="getEasyJdbcTemplate";

    }
}
