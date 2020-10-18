/*
 * Copyright (c) 2018  huipei.x
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


import com.xphsc.easyjdbc.core.SimpleJdbcDao;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.util.LogUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huipei.x
 * @date  2018-8-20
 * @description
 */
public abstract class AbstractDaoMethodProcessor<T>  {


    protected Annotation[][] parameterAnnotations;

    protected Object[] parameters;

    protected Method method;

    protected T annotation;

    protected SimpleJdbcDao simpleJdbcDao;

    protected Map<String, Object> paramsMap = new HashMap<String, Object>(16);

    protected  Class<?> persistentClass;

    public abstract Object process();

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public T getAnnotation() {
        return annotation;
    }

    public void setAnnotation(T annotation) {
        this.annotation = annotation;
    }

    @SuppressWarnings("unchecked")
   public Class<T> getParameterizedType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public SimpleJdbcDao getSimpleJdbcDao() {
        return simpleJdbcDao;
    }

    public void setSimpleJdbcDao(SimpleJdbcDao simpleJdbcDao) {
        this.simpleJdbcDao = simpleJdbcDao;
    }

    public Annotation[][] getParameterAnnotations() {
        return parameterAnnotations;
    }

    public Map<String, Object> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, Object> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public void setParameterAnnotations(Annotation[][] parameterAnnotations) {
        this.parameterAnnotations = parameterAnnotations;
    }

    public Class<?> getPersistentClass() {
        return persistentClass;
    }

    public void setPersistentClass(Class<?> persistentClass) {
        this.persistentClass = persistentClass;
    }
}
