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

import com.xphsc.easyjdbc.core.exception.EasyJdbcException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by ${huipei.x}
 */
public class Collects {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Iterable<?> iterable) {
        return null == iterable || isEmpty(iterable.iterator());
    }

    public static boolean isEmpty(Iterator<?> iterable) {
        return null == iterable || !iterable.hasNext();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(Iterable<?> iterable) {
        return null != iterable && isNotEmpty(iterable.iterator());
    }

    public static boolean isNotEmpty(Iterator<?> iterable) {
        return null != iterable && iterable.hasNext();
    }


    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }


    public static <E> List<E> removeAll(Collection<E> collection, Collection<?> remove) {
        ArrayList list = new ArrayList();
        Iterator i = collection.iterator();

        while(i.hasNext()) {
            Object obj = i.next();
            if(!remove.contains(obj)) {
                list.add(obj);
            }
        }
        return list;
    }
    public static <T> List<T> swapPosition(List<T> list, int oldPosition, int newPosition){
        if(null == list){
            throw new EasyJdbcException("The list can not be empty...");
        }

        if(oldPosition < newPosition){
            for(int i = oldPosition; i < newPosition; i++){
                Collections.swap(list, i, i + 1);
            }
        }

        if(oldPosition > newPosition){
            for(int i = oldPosition; i > newPosition; i--){
                Collections.swap(list, i, i - 1);
            }
        }
        return null;
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }
    public static <T> T[] toArray(T... arrays) {
        return toArray(arrays);
    }

    public static <K> String getStringValue(Map<? super K, ?> map, K key) {
        Number value = getNumber(map, key);
        return value == null?null:String.valueOf(value);
    }

    public static <K> Integer getInteger(Map<? super K, ?> map, K key) {
        Number value = getNumber(map, key);
        return value == null?null:(value instanceof Integer?(Integer)value:Integer.valueOf(value.intValue()));
    }
    public static boolean containsKey(Map<?, ?> map, String key) {
        return isNotEmpty(map)&&StringUtil.isNotBlank(key)&&map.containsKey(key)?true:false;
    }
    public static <K> Number getNumber(Map<? super K, ?> map, K key) {
        if(map != null) {
            Object value = map.get(key);
            if(value != null) {
                if(value instanceof Number) {
                    return (Number)value;
                }

                if(value instanceof String) {
                    try {
                        String e = (String)value;
                        return NumberFormat.getInstance().parse(e);
                    } catch (ParseException var4) {
                        ;
                    }
                }
            }
        }

        return null;
    }

}
