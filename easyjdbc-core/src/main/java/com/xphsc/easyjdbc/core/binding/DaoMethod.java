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
import com.xphsc.easyjdbc.core.lambda.LambdaSupplier;
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
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: DaoMethod类用于封装DAO接口中方法的执行逻辑
 * 主要负责根据方法定义创建相应的处理器，并执行对应的数据库操作
 */
public class DaoMethod {

    private Class<?> modelClass;
    private AbstractDaoMethodProcessor daoMethodProcessor;
    private Method method;
    private Object[] parameters;
    private SimpleJdbcDao simpleJdbcDao;

    public DaoMethod(Class<?> daoInterface, LambdaSupplier<EasyJdbcTemplate> easyJdbcTemplate, Method method, Object[] parameters) throws Exception {
        simpleJdbcDao = new SimpleJdbcDao();
        easyJdbcTemplate.get().interfaceClass(daoInterface.getName() + "." + method.getName());
        modelClass = getGenericEntityClass(EasyJdbcDao.class, daoInterface);
        simpleJdbcDao.easyJdbcTemplate(easyJdbcTemplate);
        simpleJdbcDao.modelClass = modelClass;
        this.method = method;
        this.parameters = parameters;
    }


    protected Object doExecute() throws Exception {
        Annotation[] annotations = method.getAnnotations();
        MethodReturnType methodReturnType = new MethodReturnType(method);
        if (methodReturnType.returnsAnnotationType) {
            for (Annotation each : annotations) {
                daoMethodProcessor = new AnnotationMethodProcessor();
                if (daoMethodProcessor != null) {
                    daoMethodProcessor.setAnnotation(each);
                    daoMethodProcessor.setParameters(parameters);
                    daoMethodProcessor.setParameterAnnotations(method.getParameterAnnotations());
                    daoMethodProcessor.setSimpleJdbcDao(simpleJdbcDao);
                    daoMethodProcessor.setMethod(method);
                    daoMethodProcessor.setParamsMap(initParamsMap(method, parameters));
                    daoMethodProcessor.setPersistentClass(modelClass);
                    return daoMethodProcessor.process();
                }
            }
        } else {
            daoMethodProcessor = new BaseMethodProcessor();
            daoMethodProcessor.setMethod(method);
            daoMethodProcessor.setParameters(parameters);
            daoMethodProcessor.setSimpleJdbcDao(simpleJdbcDao);
            daoMethodProcessor.setPersistentClass(modelClass);
            return daoMethodProcessor.process();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericEntityClass(Class<?> targetInterface, Class<?> actualClass) {
        for (Type type : actualClass.getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type rawType = parameterizedType.getRawType();

                if (rawType instanceof Class && targetInterface.isAssignableFrom((Class<?>) rawType)) {
                    Type actualType = parameterizedType.getActualTypeArguments()[0];
                    if (actualType instanceof Class) {
                        return (Class<T>) actualType;
                    }
                }
            }
        }

        // 递归查找父类接口
        Class<?> superClass = actualClass.getSuperclass();
        if (superClass != null && !Object.class.equals(superClass)) {
            return getGenericEntityClass(targetInterface, superClass);
        }

        return null;
    }


    public static String[] getMethodParameterNamesByAnnotation(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return new String[0];
        }

        String[] parameterNames = new String[parameterAnnotations.length];

        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof SqlParam) {
                    parameterNames[i] = ((SqlParam) annotation).value();
                    break; // 若有多个注解，只取第一个 SqlParam
                }
            }
        }

        return parameterNames;
    }

    private Map<String, Object> sqlParamsMap;
    private PageInfo pageInfo;

    private Map<String, Object> initParamsMap(Method method, Object[] args) throws Exception {
        this.sqlParamsMap = new HashMap<>();
        this.pageInfo = new PageInfo();

        if (args == null || args.length == 0) {
            return sqlParamsMap;
        }
        String[] params = getMethodParameterNamesByAnnotation(method);
        // 带 @SqlParam 的参数
        if (params != null && params[0] != null) {
            Assert.isTrue(params.length == args.length,
                    "Method parameter number mismatch: all parameters must be annotated with @SqlParam if using multiple arguments.");

            for (int i = 0; i < params.length; i++) {
                String paramName = params[i];
                Object argValue = args[i];

                Assert.notNull(paramName, "Each parameter must use @SqlParam annotation.");

                if ("pageNum".equalsIgnoreCase(paramName)) {
                    pageInfo.setPageNum(Integer.parseInt(argValue.toString()));
                }
                if ("pageSize".equalsIgnoreCase(paramName)) {
                    pageInfo.setPageSize(Integer.parseInt(argValue.toString()));
                }

                sqlParamsMap.put(paramName, argValue);
            }

        } else {
            // 无注解方式：只处理第一个参数
            Object arg = args[0];
            Class<?> paramType = method.getParameterTypes()[0];

            if (Map.class.isAssignableFrom(paramType)) {
                sqlParamsMap = (Map<String, Object>) arg;

            } else if (PageInfo.class.isAssignableFrom(paramType)) {
                this.pageInfo = (PageInfo) arg;
                mergePageInfoIntoParams(this.pageInfo);

            } else {
                // POJO 参数
                sqlParamsMap.put("POJO", arg);
            }
        }

        return sqlParamsMap;
    }

    private void mergePageInfoIntoParams(PageInfo pageInfo) {
        if (pageInfo == null) return;
        if (pageInfo.getPageNum() >= 1 && pageInfo.getOffset() == -1) {
            sqlParamsMap.put("pageNum", pageInfo.getPageNum());
        }
        if (pageInfo.getPageSize() > 0) {
            sqlParamsMap.put("pageSize", pageInfo.getPageSize());
        }
        if (pageInfo.getOffset() >= 0) {
            sqlParamsMap.put("offset", pageInfo.getOffset());
        }
        if (pageInfo.getLimit() > 0) {
            sqlParamsMap.put("limit", pageInfo.getLimit());
        }
    }

    private static class MethodReturnType {
        private boolean returnsAnnotationType;

        public MethodReturnType(Method method) {
            SQLOptionTypeParser sqlOptionTypeParser = new DefaultSQLOptionTypeParser();
            SQLOptionType sqlOptionType = sqlOptionTypeParser.getSqlCommandType(method);
            if (sqlOptionType.equals(SQLOptionType.SQLINSERT) ||
                    sqlOptionType.equals(SQLOptionType.SQLDELETE) ||
                    sqlOptionType.equals(SQLOptionType.SQLUPDATE) ||
                    sqlOptionType.equals(SQLOptionType.SQLSELECT)) {
                this.returnsAnnotationType = true;
            }


        }
    }
}
