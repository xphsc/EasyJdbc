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
package com.xphsc.easyjdbc.core.processor;







import com.xphsc.easyjdbc.annotation.*;
import com.xphsc.easyjdbc.core.exception.EasyJdbcException;
import com.xphsc.easyjdbc.core.metadata.SQLOptionType;
import com.xphsc.easyjdbc.core.parser.*;
import com.xphsc.easyjdbc.util.Collects;
import com.xphsc.easyjdbc.util.StringUtil;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


/**
 * @author huipei.x
 * @date  2018-8-20
 * @description
 */
public class AnnotationMethodProcessor extends AbstractDaoMethodProcessor {

    private Method providerMethod;
    private  Class<?> providerType;
    private String sql="";
    private String providerMethodName = null;
    private boolean useGeneratedKeys=false;
    private String keyProperty=null;
    private boolean hasInsertOrUpdatePlaceHolder=false;
    @Override
    public Object process() {

        Object[] result=null;
         getAnnotationType();
        SQLOptionTypeParser sqlOptionTypeParser
        =new DefaultSQLOptionTypeParser();
        SQLOptionType sqlOptionType
        = sqlOptionTypeParser.getSqlCommandType(method);
        if  (
            providerType!=null
            ){
            for (Method m : this.providerType.getMethods()) {
                if (providerMethodName.equals(m.getName())) {
                    if (m.getReturnType() == String.class) {
                        if (providerMethod != null){
                            throw new EasyJdbcException("Error creating Sql for SqlProvider. Method '"
                                    + providerMethodName + "' is found multiple in SqlProvider '" + this.providerType.getName()
                                    + "'. Sql provider method can not overload.");
                        }
                        this.providerMethod = m;
                    }
                }
            }
            Map<String, Object> params=null;
            try {
                if(!Collects.isEmpty(providerMethod.getParameterTypes())){
                    Class<?> parameterType=providerMethod.getParameterTypes()[0];
                    if(parameterType.isAssignableFrom(Map.class)){
                        params=  paramsMap;
                        sql = (String) providerMethod.invoke(providerType.newInstance(),params);
                    }
                }else{
                    sql = (String) providerMethod.invoke(providerType.newInstance());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        if(
           sqlOptionType.equals(SQLOptionType.SQLSELECT)
        ){
            SQLSelectParser sqlSelectParser=new DefaultSQLSelectParser();
            return sqlSelectParser.select(
                    sql,
                    simpleJdbcDao,
                    persistentClass,
                    method,
                    paramsMap
            );

        }

        SQLParser sqlParser=new DefaultSQLParser();
        if(hasInsertOrUpdatePlaceHolder){
            result =sqlParser.sqlPlaceHolder(sql, paramsMap, false);
        }else if(sqlParser.hasOgnlPlaceHolder(sql)){
            result =sqlParser.sqlPlaceHolder(sql, paramsMap, true);

        }else{
            result =sqlParser.sqlPlaceHolder(sql, null, true);
        }

        Object returnResult = null;
        Class<?> returnType = method.getReturnType();
        boolean returnsOptional = Optional.class.equals(returnType);
        if (sqlOptionType.equals(SQLOptionType.SQLUPDATE)
                ){
            if(hasInsertOrUpdatePlaceHolder){
                 String sql=  (String) result[0];
                returnResult=Collects.isNotEmpty(paramsMap)?simpleJdbcDao.getEasyJdbcTemplate().getJdbcBuilder().update((String) result[0], (Object[]) result[1]):
                      simpleJdbcDao.getEasyJdbcTemplate().getJdbcBuilder().update(sql);

            }
            if(returnsOptional){
                return  Optional.ofNullable(returnResult);
            }else {
                return returnResult instanceof int[]?((int[]) returnResult).length:returnResult;
            }
        }

        if(
            sqlOptionType.equals(SQLOptionType.SQLINSERT)||
            sqlOptionType.equals(SQLOptionType.SQLDELETE)
            ){
            if(useGeneratedKeys){
                KeyHolder keyHolder = new GeneratedKeyHolder();
                final String finalKeyProperty = keyProperty;
                final Object[] finalResult = result;
                final Object[] finalResult1 = result;
                simpleJdbcDao.getEasyJdbcTemplate().getJdbcBuilder().update(
                        new PreparedStatementCreator() {
                            @Override
                            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                                PreparedStatement ps = con.prepareStatement((String) finalResult[0], new String[]{finalKeyProperty});
                                int i = 1;
                                for (Object object : (Object[]) finalResult1[1]) {
                                    ps.setObject(i, object);
                                    i++;
                                }
                                return ps;
                            }
                        },
                        keyHolder);
                Object key=null;
                returnResult=getKey(key, method, keyHolder);
            }else{
                if(hasInsertOrUpdatePlaceHolder){
                    returnResult=simpleJdbcDao.getEasyJdbcTemplate().getJdbcBuilder().update((String) result[0], (Object[]) result[1]);
                }


            }
        }
        if(returnsOptional){
            return  Optional.ofNullable(returnResult);
        }else {
            return returnResult;
        }
    }




    private Object getKey(Object key ,Method method, KeyHolder keyHolder){
        ReturnKeyType returnKeyType=new  ReturnKeyType(method);
        if(returnKeyType.returnsInteger) {
            key=keyHolder.getKey().intValue();
        }
        if(returnKeyType.returnsLong) {
            key=keyHolder.getKey().longValue();
        }
        if(returnKeyType.returnsShort
                ) {
            key=keyHolder.getKey().shortValue();
        }
        if(returnKeyType.returnsDouble) {
            key=keyHolder.getKey().doubleValue();
        }
        if(returnKeyType.returnsByte
                ) {
            key=keyHolder.getKey().byteValue();
        }
        if(returnKeyType.returnsObject) {
            key=keyHolder.getKey();
        }
        return key;
    }


    private  void getAnnotationType(){
            if (annotation instanceof SqlInsert) {
                hasInsertOrUpdatePlaceHolder=true;
                SqlInsert sqlInsert = (SqlInsert) annotation;
                sql = sqlInsert.value();
            }
        Annotation[] annotations=method.getAnnotations();
        for(Annotation annotation: annotations){
            if(annotation.annotationType().equals(SqlOptions.class)){
                SqlOptions sqlOptions = (SqlOptions) annotation;
                useGeneratedKeys=sqlOptions.useGeneratedKeys();

                if(StringUtil.isNotBlank(sqlOptions.keyProperty())){
                    keyProperty=sqlOptions.keyProperty();
                }else{
                    keyProperty="id";
                }
            };
        }

            if (annotation instanceof SqlUpdate) {
                hasInsertOrUpdatePlaceHolder=true;
                SqlUpdate  sqlUpdate = (SqlUpdate) annotation;
                sql = sqlUpdate.value();
            }
            if (annotation instanceof SqlDelete) {
                SqlDelete  sqlDelete = (SqlDelete) annotation;
                sql = sqlDelete.value();
            }
            if (annotation instanceof SqlUpdateProvider) {
                SqlUpdateProvider sqlUpdateProvider = (SqlUpdateProvider) annotation;
                this.providerType = (Class<?>)sqlUpdateProvider.type();
                providerMethodName = (String) sqlUpdateProvider.method();
                if(sqlUpdateProvider.returnType()!=null){
                    persistentClass = sqlUpdateProvider.returnType();
                }
            }
            if (annotation instanceof SqlInsertProvider) {
                SqlInsertProvider    sqlInsertProvider = (SqlInsertProvider) annotation;
                this.providerType = (Class<?>)sqlInsertProvider.type();
                providerMethodName = (String) sqlInsertProvider.method();
                if(sqlInsertProvider.returnType()!=null){
                    persistentClass = sqlInsertProvider.returnType();
                }
            }
            if (annotation instanceof SqlDeleteProvider) {
                SqlDeleteProvider sqlDeleteProvider = (SqlDeleteProvider) annotation;
                this.providerType = (Class<?>)sqlDeleteProvider.type();
                providerMethodName = (String) sqlDeleteProvider.method();
                if(sqlDeleteProvider.returnType()!=null){
                    persistentClass = sqlDeleteProvider.returnType();
                }
            }
            if (annotation instanceof SqlSelect) {
                SqlSelect sqlSelect = (SqlSelect) annotation;
                 sql = sqlSelect.value();
                if(sqlSelect.entityClass()!=null&&sqlSelect.entityClass()!=void.class){
                    persistentClass = sqlSelect.entityClass();
                }
            }

            if (annotation instanceof SqlSelectProvider) {
                SqlSelectProvider sqlSelectProvider = (SqlSelectProvider) annotation;
                this.providerType = (Class<?>)sqlSelectProvider.type();
                providerMethodName = (String) sqlSelectProvider.method();
                if(sqlSelectProvider.entityClass()!=null&&sqlSelectProvider.entityClass()!=void.class){
                    persistentClass = sqlSelectProvider.entityClass();
                }
            }

    }

    public static class ReturnKeyType {
        private boolean returnsInteger;
        private boolean returnsLong;
        private boolean returnsShort;
        private boolean returnsDouble;
        private boolean returnsByte;
        private boolean returnsObject;


        public ReturnKeyType(Method method) {
            Class<?> returnType = method.getReturnType();
            if (returnType.isAssignableFrom(Integer.class)||
                    returnType.isAssignableFrom(int.class)) {
                this.returnsInteger = true;
            }
            if (returnType.isAssignableFrom(Long.class)||
                    returnType.isAssignableFrom(long.class)) {
                this.returnsLong = true;
            }
            if (returnType.isAssignableFrom(short.class)||
                    returnType.isAssignableFrom(Short.class)) {
                this.returnsShort = true;
            }

            if (returnType.isAssignableFrom(double.class)||
                    returnType.isAssignableFrom(Double.class)) {
                this.returnsDouble = true;
            }

            if (returnType.isAssignableFrom(byte.class)||
                    returnType.isAssignableFrom(Byte.class)) {
                this.returnsByte = true;
            }
            if (returnType.isAssignableFrom(Object.class)) {
                this.returnsObject = true;
            }


        }

}

}

