/*
 * Copyright (c) 2018 huipei.x
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
package com.xphsc.easyjdbc.util;


import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;
import org.springframework.objenesis.instantiator.ObjectInstantiator;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ${huipei.x}
 */
public class Jdbcs {

    // 实例化工具
    private static final Objenesis OBJENESIS = new ObjenesisStd();
    // 实例构造器缓存
    private static final ConcurrentHashMap<String, ObjectInstantiator<?>> INSTANTIATORS = new ConcurrentHashMap<String, ObjectInstantiator<?>>();


    /**
     * 获取类及其父类中定义的字段
     */
    public static Set<Field> getFields(final Class<?> cls,final Set<Class<?>> superclass) {
        Set<Field> fields = new HashSet<Field>();
        for (Class<?> type : superclass) {
            fields.addAll(Arrays.asList(type.getDeclaredFields()));
        }
        fields.addAll(Arrays.asList(cls.getDeclaredFields()));
        return Collections.unmodifiableSet(fields);
    }


    /**
     * 获取字段的读方法
     */
    public static Method getReadMethod(final Class<?> cls,final Set<Class<?>> superclass
            ,final String fieldName,Class<?>... parameterTypes){

        Set<Class<?>> classes = new HashSet();
        classes.add(cls);
        classes.addAll(superclass);
        String methodName = "get"+ StringUtil.capitalize(fieldName);
        Method method = getMethod(classes,methodName,parameterTypes);
        if(null == method){
            methodName = "is" + StringUtil.capitalize(fieldName);
            method = getMethod(classes, methodName,parameterTypes);
        }
        return method;
    }

    /**
     * 获取字段的写方法
     */
    public static Method getWriteMethod(final Class<?> cls,final Set<Class<?>> superclass
            , final String fieldName,Class<?>... parameterTypes){

        Set<Class<?>> classes = new HashSet();
        classes.add(cls);
        classes.addAll(superclass);
        String methodName = "set" + StringUtil.capitalize(fieldName);
        return getMethod(classes, methodName,parameterTypes);
    }

    /**
     * 获取方法
     * @param classes
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method getMethod(final Set<Class<?>> classes
            , final String methodName,Class<?>... parameterTypes) {
        for (Class<?> i : classes) {
            try {
                Method method = i.getDeclaredMethod(methodName, parameterTypes);
                if ((!Modifier.isPublic(method.getModifiers())
                        || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                        && !method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method;
            } catch (NoSuchMethodException e) {}
        }
        return null;
    }

    /**
     * 方法调用
     */
    public static Object invokeMethod(Object object,Method method,String errorMsg, Object... args){
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(errorMsg,e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(errorMsg,e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(errorMsg,e);
        }
    }

    @SuppressWarnings("all")
    public static <T> T newInstance(final Class<?> cls) {
        if(cls.isInterface()){
            throw new IllegalArgumentException("不是有效的类型");
        }
        if (INSTANTIATORS.contains(cls.getName())){
            return (T) INSTANTIATORS.get(cls.getName()).newInstance();
        }
        ObjectInstantiator instantiator = OBJENESIS.getInstantiatorOf(cls);
        INSTANTIATORS.putIfAbsent(cls.getName(), instantiator);
        return (T) instantiator.newInstance();
    }



    /**
     * 字段名称由驼峰格式转换为下划线格式
     */
    public static String camelToUnderline(String fieldName) {
        int len = fieldName.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = fieldName.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }




}
