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
package com.xphsc.easyjdbc.core.binding;

import com.xphsc.easyjdbc.EasyJdbcTemplate;
import com.xphsc.easyjdbc.annotation.*;
import com.xphsc.easyjdbc.core.EasyJdbcDao;
import com.xphsc.easyjdbc.core.SimpleJdbcDao;
import com.xphsc.easyjdbc.core.metadata.SQLOptionType;
import com.xphsc.easyjdbc.core.parser.DefaultSQLOptionTypeParser;
import com.xphsc.easyjdbc.core.parser.SQLOptionTypeParser;
import com.xphsc.easyjdbc.core.processor.*;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.util.Assert;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huipei.x
 * @date  2018-8-20
 * @description  :
 */
public class DaoMethod {

    private Class<?> modelClass;
    private AbstractDaoMethodProcessor daoMethodProcessor=null;
    private Method method;
    private Object[] parameters;
    private SimpleJdbcDao simpleJdbcDao=new SimpleJdbcDao();
    public DaoMethod(Class<?> daoInterface, EasyJdbcTemplate easyJdbcTemplate, Method method, Object[] parameters) throws Exception {
        easyJdbcTemplate.interfaceClass(daoInterface.getName()+"."+method.getName());
        modelClass= getEntityClass(EasyJdbcDao.class, daoInterface);
        simpleJdbcDao.setEasyJdbcTemplate(easyJdbcTemplate);
        simpleJdbcDao.modelClass=modelClass;
        this.method=method;
        this.parameters=parameters;
    }


    protected Object doExecute() throws Exception {
        Annotation[] annotations = method.getAnnotations();
        SQLOptionTypeParser sqlOptionTypeParser=new DefaultSQLOptionTypeParser();
        SQLOptionType sqlOptionType= sqlOptionTypeParser.getSqlCommandType(method);
        if(annotations.length!=0){
            for(Annotation each :annotations){
                if(
                    sqlOptionType.equals(SQLOptionType.SQLINSERT)||
                    sqlOptionType.equals(SQLOptionType.SQLDELETE)||
                    sqlOptionType.equals(SQLOptionType.SQLUPDATE)||
                    sqlOptionType.equals(SQLOptionType.SQLSELECT)

                ){
                    daoMethodProcessor=new AnnotationMethodProcessor();
                }

                if(daoMethodProcessor!=null){
                    daoMethodProcessor.setAnnotation(each);
                    daoMethodProcessor.setParameters(parameters);
                    daoMethodProcessor.setParameterAnnotations(method.getParameterAnnotations());
                    daoMethodProcessor.setSimpleJdbcDao(simpleJdbcDao);
                    daoMethodProcessor.setMethod(method);
                    daoMethodProcessor.setParamsMap(initParamsMap(method, parameters));
                    daoMethodProcessor.setPersistentClass(modelClass);
                    return  daoMethodProcessor.process();
                }
            }
        } else{
            daoMethodProcessor=new BaseMethodProcessor();
            daoMethodProcessor.setMethod(method);
            daoMethodProcessor.setParameters(parameters);
            daoMethodProcessor.setSimpleJdbcDao(simpleJdbcDao);
            daoMethodProcessor.setPersistentClass(modelClass);
            return  daoMethodProcessor.process();
        }

        return null;
    }

    public static Class getEntityClass(Class commonClass,Class mapperClass){
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) type;
                if (t.getRawType().equals(commonClass) || commonClass.isAssignableFrom((Class<?>) t.getRawType())) {
                    Class<?> returnType = (Class<?>) t.getActualTypeArguments()[0];
                    return returnType;
                }
            }
        }
        return null;
    }

    public static String[] getMethodParameterNamesByAnnotation(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return null;
        }
        String[] parameterNames = new String[parameterAnnotations.length];

        int i = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof SqlParam) {
                    SqlParam param = (SqlParam) annotation;
                    parameterNames[i++] = param.value();
                }
            }
        }
        return parameterNames;
    }

    private Map<String, Object> sqlParamsMap;
    private PageInfo pageInfo;

    private Map<String, Object> initParamsMap(Method method, Object[] args) throws Exception {
        sqlParamsMap= new HashMap<String, Object>();
        pageInfo=new PageInfo();
        if (args != null && args.length >= 1) {
            String[]  params =getMethodParameterNamesByAnnotation(method);
            Assert.isTrue(params.length == args.length, "Method parameter number >= 2, parameter must be used: label @param!");
            if(params!=null&&params[0]!=null){
                int argsNum = 0;
                for (String v : params) {
                    Assert.notNull(v,"Dao interface definition, parameter using @param tag!");
                    if ("pageNum".equalsIgnoreCase(v)) {
                        pageInfo.setPageNum(Integer.parseInt(args[argsNum].toString()));
                    }
                    if ("pageSize".equalsIgnoreCase(v)) {
                        pageInfo.setPageSize(Integer.parseInt(args[argsNum].toString()));
                    }
                    sqlParamsMap.put(v, args[argsNum]);
                    argsNum++;
                }

            }

            if(params[0]==null){
                Class<?> parameterType=method.getParameterTypes()[0];
                if(parameterType.isAssignableFrom(Map.class)){
                    sqlParamsMap= (Map<String, Object>) args[0];
                }
                if(parameterType.isAssignableFrom(PageInfo.class)){
                    this.pageInfo= (PageInfo) args[0];
                    if(pageInfo!=null){
                        if(pageInfo.getPageNum()>=1&&pageInfo.getOffset()==-1){
                            sqlParamsMap.put("pageNum", pageInfo.getPageNum());
                        }
                        if(pageInfo.getPageSize()>0){
                            sqlParamsMap.put("pageSize",pageInfo.getPageSize());
                        }
                        if(pageInfo.getOffset()>=0){
                            sqlParamsMap.put("offset", pageInfo.getOffset());
                        }
                        if(pageInfo.getLimit()>0){
                            sqlParamsMap.put("limit",pageInfo.getPageSize());
                        }
                    }
                }

            }

        } else if (args != null && args.length == 1) {
            sqlParamsMap.put("POJO", args[0]);
        }
        return sqlParamsMap;
    }



}
