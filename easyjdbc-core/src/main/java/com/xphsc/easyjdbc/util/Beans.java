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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author huipei.x
 * @date  2018-8-20
 * @description
 */
public class Beans {

    public static Map<String, Object> beanToMap(Object bean){
        if (bean == null) {
            return (new java.util.HashMap<>());
        }
        Map<String, Object> beanMap = new HashMap<>();

        PropertyDescriptor[] descriptors = getPropertyDescriptors(bean);
        for (PropertyDescriptor descriptor : descriptors) {
            String name = descriptor.getName();
            Method readMethod = descriptor.getReadMethod();
            if (readMethod != null) {
                try {
                    beanMap.put(name, readMethod.invoke(bean));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return beanMap;
    }

    public static TreeMap<String, Object> beanToTreeMap(Object bean){
        if (bean == null) {
            return (new java.util.TreeMap<>());
        }
        TreeMap<String, Object> beanMap = new TreeMap<>();

        PropertyDescriptor[] descriptors = getPropertyDescriptors(bean);
        for (PropertyDescriptor descriptor : descriptors) {
            String name = descriptor.getName();
            Method readMethod = descriptor.getReadMethod();
            if (readMethod != null) {
                try {
                    beanMap.put(name, readMethod.invoke(bean));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return beanMap;
    }


    public static PropertyDescriptor[] getPropertyDescriptors(Object bean) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(bean.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        if (descriptors == null) {
            descriptors = new PropertyDescriptor[0];
        }
        return descriptors;
    }
}
