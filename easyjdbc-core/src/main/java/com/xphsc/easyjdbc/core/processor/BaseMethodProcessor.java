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



import com.xphsc.easyjdbc.core.entity.Sorts;
import com.xphsc.easyjdbc.core.parser.DefaultKeyHolderParser;
import com.xphsc.easyjdbc.core.parser.KeyHolderParser;
import com.xphsc.easyjdbc.page.PageInfo;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: BaseMethodProcessor类继承自AbstractDaoMethodProcessor
 * 用于处理自定的DAO的基本方法
 */
public class BaseMethodProcessor extends AbstractDaoMethodProcessor {

    private PageInfo pageInfo;

    @Override
    public Object process() {
        simpleJdbcDao.modelClass=persistentClass;
        MethodName methodName=new MethodName(method);
          if(this.parameters!=null){
              MethodParameterType methodParameterType=new MethodParameterType(method);
              if(methodParameterType.isPage()){
                  if (methodName.isFindAll()) {
                      pageInfo= (PageInfo) parameters[0];
                      Sorts sort=null;
                      if(parameters.length==2){
                          sort= (Sorts) parameters[1];
                      }
                      return sort==null?simpleJdbcDao.findAll(pageInfo):simpleJdbcDao.findAll(pageInfo,sort);
                  }
              }
              if(methodParameterType.isSorts()){
                  if(methodName.isFindAll()){
                      Sorts sort= (Sorts) parameters[0];
                      return simpleJdbcDao.findAll(sort);
                  }
              }

              if(methodParameterType.isSerializable()){
                  if(methodName.isGetByPrimaryKey()){
                      return simpleJdbcDao.getByPrimaryKey((Serializable) parameters[0]);
                  }
                  if(methodName.isExists()){
                      return simpleJdbcDao.exists((Serializable) parameters[0]);
                  }
                  if(methodName.isGetById()){
                      return simpleJdbcDao.getById((Serializable) parameters[0]);
                  }
                  if(methodName.isDeleteByPrimaryKey()){
                      return simpleJdbcDao.deleteByPrimaryKey((Serializable) parameters[0]);
                  }
              }
              if(methodParameterType.isObject()){
                  if(methodName.isInsert()){
                      return simpleJdbcDao.insert(parameters[0]);

                  }else if(methodName.isInsertForKey()){
                      return simpleJdbcDao.insertForKey(parameters[0]);
                  }
                  else if(methodName.isUpdate()){
                      return simpleJdbcDao.update(parameters[0]);
                  }
                  else if(methodName.isUpdateWithNull()){
                      return simpleJdbcDao.updateWithNull(parameters[0]);
                  }
                  else if(methodName.isDelete()){
                      return simpleJdbcDao.delete(parameters[0]);
                  }
              }
              if(methodParameterType.isIterable()) {
                  if(methodName.isFindByIds()){
                      return simpleJdbcDao.findByIds((Iterable) parameters[0]);
                  }
                  if(methodName.isDeleteByIds()){
                      return simpleJdbcDao.deleteByIds((Iterable) parameters[0]);
                  }
              }

              if(methodParameterType.isList()) {
                  if(methodName.isBatchInsert()){
                      return simpleJdbcDao.batchInsert((List) parameters[0]);
                  }
                  if(methodName.isBatchUpdate()){
                      return simpleJdbcDao.batchUpdate((List) parameters[0]);
                  }
              }


          }else{
              if(methodName.isFindAll()){
                  return simpleJdbcDao.findAll();
              }
              if(methodName.isCount()) {
                  return simpleJdbcDao.count();
              }
              if(methodName.isExample()){
                  return simpleJdbcDao.example();
              }
              if(methodName.isSelector()){
                  return  simpleJdbcDao.selector();
              }
              if(methodName.isGetEasyJdbcTemplate()){
                  return  simpleJdbcDao.getEasyJdbcTemplate();
              }
              if(methodName.isGetCacheClear()){
                  simpleJdbcDao.cacheClear();
              }
          }

        return null;
    }

    public static class MethodParameterType {
        private enum ParamType {
            SERIALIZABLE,
            OBJECT,
            PAGE,
            LIST,
            SORTS,
            ITERABLE
        }
        private final EnumSet<ParamType> types = EnumSet.noneOf(ParamType.class);

        public MethodParameterType(Method method) {
            Class<?> paramType = method.getParameterTypes()[0];

            if (Sorts.class.isAssignableFrom(paramType)) {
                types.add(ParamType.SORTS);
            }
            if (Serializable.class.isAssignableFrom(paramType)) {
                types.add(ParamType.SERIALIZABLE);
            }
            if (PageInfo.class.isAssignableFrom(paramType)) {
                types.add(ParamType.PAGE);
            }
            if (List.class.isAssignableFrom(paramType)) {
                types.add(ParamType.LIST);
            }
            if (Iterable.class.isAssignableFrom(paramType)) {
                types.add(ParamType.ITERABLE);
            }
            if (Object.class.isAssignableFrom(paramType)) {
                types.add(ParamType.OBJECT);
            }
        }

        public boolean is(ParamType type) {
            return types.contains(type);
        }

        public boolean isSerializable() { return is(ParamType.SERIALIZABLE); }
        public boolean isPage()         { return is(ParamType.PAGE); }
        public boolean isSorts()        { return is(ParamType.SORTS); }
        public boolean isList()         { return is(ParamType.LIST); }
        public boolean isIterable()     { return is(ParamType.ITERABLE); }
        public boolean isObject()       { return is(ParamType.OBJECT); }
    }


    public static class MethodName {
        private final EnumSet<BaseMethodType> types = EnumSet.noneOf(BaseMethodType.class);

        public MethodName(Method method) {
            BaseMethodType type = methodNameMap.get(method.getName());
            if (type != null) {
                types.add(type);
            }
        }

        public boolean is(BaseMethodType type) {
            return types.contains(type);
        }

        public boolean isFindAll() {
            return is(BaseMethodType.FIND_ALL);
        }

        public boolean isExists() {
            return is(BaseMethodType.EXISTS);
        }
        public boolean isGetByPrimaryKey() {
            return is(BaseMethodType.GET_BY_PRIMARY_KEY);
        }

        public boolean isGetById() {
            return is(BaseMethodType.GET_BY_ID);
        }
        public boolean isDeleteByPrimaryKey() {
            return is(BaseMethodType.DELETE_BY_PRIMARY_KEY);
        }
        public boolean isInsert() {
            return is(BaseMethodType.INSERT);
        }

        public boolean isInsertForKey() {
            return is(BaseMethodType.INSERT_FOR_KEY);
        }
        public boolean isUpdate() {
            return is(BaseMethodType.UPDATE);
        }
        public boolean isUpdateWithNull() {
            return is(BaseMethodType.UPDATE_WITH_NULL);
        }
        public boolean isFindByIds() {
            return is(BaseMethodType.FIND_BY_IDS);
        }
        public boolean isDeleteByIds() {
            return is(BaseMethodType.DELETE_BY_IDS);
        }
        public boolean isDelete() {
            return is(BaseMethodType.DELETE);
        }
        public boolean isBatchInsert() {
            return is(BaseMethodType.BATCH_INSERT);
        }

        public boolean isBatchUpdate() {
            return is(BaseMethodType.BATCH_UPDATE);
        }

        public boolean isCount() {
            return is(BaseMethodType.COUNT);
        }

        public boolean isExample() {
            return is(BaseMethodType.EXAMPLE);
        }
        public boolean isSelector() {
            return is(BaseMethodType.SELECTOR);
        }
        public boolean isGetEasyJdbcTemplate() {
            return is(BaseMethodType.GET_EASY_JDBC_TEMPLATE);
        }
        public boolean isGetCacheClear() {
            return is(BaseMethodType.CLEAR);
        }

    }
    private static final Map<String, BaseMethodType> methodNameMap = new HashMap<>();
    static {
        methodNameMap.put(BaseMethodConstants.FIND_ALL, BaseMethodType.FIND_ALL);
        methodNameMap.put(BaseMethodConstants.EXISTS, BaseMethodType.EXISTS);
        methodNameMap.put(BaseMethodConstants.GET_BY_PRIMARY_KEY, BaseMethodType.GET_BY_PRIMARY_KEY);
        methodNameMap.put(BaseMethodConstants.GET_BY_ID, BaseMethodType.GET_BY_ID);
        methodNameMap.put(BaseMethodConstants.DELETE_BY_PRIMARY_KEY, BaseMethodType.DELETE_BY_PRIMARY_KEY);
        methodNameMap.put(BaseMethodConstants.INSERT, BaseMethodType.INSERT);
        methodNameMap.put(BaseMethodConstants.INSERT_FOR_KEY, BaseMethodType.INSERT_FOR_KEY);
        methodNameMap.put(BaseMethodConstants.UPDATE, BaseMethodType.UPDATE);
        methodNameMap.put(BaseMethodConstants.UPDATE_WITH_NULL, BaseMethodType.UPDATE_WITH_NULL);
        methodNameMap.put(BaseMethodConstants.FIND_BY_IDS, BaseMethodType.FIND_BY_IDS);
        methodNameMap.put(BaseMethodConstants.DELETE_BY_IDS, BaseMethodType.DELETE_BY_IDS);
        methodNameMap.put(BaseMethodConstants.DELETE, BaseMethodType.DELETE);
        methodNameMap.put(BaseMethodConstants.BATCH_INSERT, BaseMethodType.BATCH_INSERT);
        methodNameMap.put(BaseMethodConstants.BATCH_UPDATE, BaseMethodType.BATCH_UPDATE);
        methodNameMap.put(BaseMethodConstants.COUNT, BaseMethodType.COUNT);
        methodNameMap.put(BaseMethodConstants.EXAMPLE, BaseMethodType.EXAMPLE);
        methodNameMap.put(BaseMethodConstants.SELECTOR, BaseMethodType.SELECTOR);
        methodNameMap.put(BaseMethodConstants.GET_EASY_JDBC_TEMPLATE, BaseMethodType.GET_EASY_JDBC_TEMPLATE);
        methodNameMap.put(BaseMethodConstants.CLEAR, BaseMethodType.CLEAR);
    }
    private enum BaseMethodType {
        FIND_ALL,
        EXISTS,
        GET_BY_PRIMARY_KEY,
        GET_BY_ID,
        DELETE_BY_PRIMARY_KEY,
        INSERT,
        INSERT_FOR_KEY,
        UPDATE,
        UPDATE_WITH_NULL,
        FIND_BY_IDS,
        DELETE_BY_IDS,
        DELETE,
        BATCH_INSERT,
        BATCH_UPDATE,
        COUNT,
        EXAMPLE,
        SELECTOR,
        GET_EASY_JDBC_TEMPLATE,
        CLEAR
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
