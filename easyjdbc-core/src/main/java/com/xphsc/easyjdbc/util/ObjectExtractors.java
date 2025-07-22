package com.xphsc.easyjdbc.util;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectExtractors {


    // 判断是否为 POJO（非系统类、集合、Map、数组、包装类型等）
    public static boolean isPojo(Object obj) {
        if (obj == null) return false;
        Class<?> clazz = obj.getClass();
        return !clazz.isArray()
                && !Collection.class.isAssignableFrom(clazz)
                && !Map.class.isAssignableFrom(clazz)
                && !clazz.isEnum()
                && !String.class.isAssignableFrom(clazz)
                && !clazz.isPrimitive()
                && !Number.class.isAssignableFrom(clazz)
                && !Boolean.class.equals(clazz)
                && !Character.class.equals(clazz);
    }

    public static boolean isPojo(Class<?> clazz) {
        if (clazz == null) return false;
        return !clazz.isArray()
                && !Collection.class.isAssignableFrom(clazz)
                && !Map.class.isAssignableFrom(clazz)
                && !clazz.isEnum()
                && !String.class.isAssignableFrom(clazz)
                && !clazz.isPrimitive()
                && !Number.class.isAssignableFrom(clazz)
                && !Boolean.class.equals(clazz)
                && !Character.class.equals(clazz);
    }

    // 提取第一个 POJO（排除指定类型），泛型返回
    public static <T> T extractFirstPojo(Map<String, Object> map, Set<Class<?>> excludeTypes, Class<T> expectedType) {
        for (Object value : map.values()) {
            if (value == null) continue;
            if (!expectedType.isAssignableFrom(value.getClass())) continue;
         if (!isPojo(value)) continue;
            if (excludeTypes != null && excludeTypes.contains(value.getClass())) continue;

            return expectedType.cast(value);
        }
        return null;
    }

    public static <T> T extract(Map<String, Object> map, Set<Class<?>> excludeTypes, Class<T> expectedType) {
        for (Object value : map.values()) {
            if (value == null) continue;
            if (!expectedType.isAssignableFrom(value.getClass())) continue;
            if (excludeTypes != null && excludeTypes.contains(value.getClass())) continue;

            return expectedType.cast(value);
        }
        return null;
    }

    // 提取所有指定类型的对象（泛型列表）
    public static <T> List<T> extractAllByType(Map<String, Object> map, Class<T> targetType) {
        return map.values().stream()
                .filter(obj -> obj != null && targetType.isAssignableFrom(obj.getClass()))
                .map(targetType::cast)
                .collect(Collectors.toList());
    }

    // 提取所有 POJO（可排除类型），返回 <key, value>
    public static Map<String, Object> extractPojoEntries(Map<String, Object> map, Set<Class<?>> excludeTypes) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value == null) continue;
            if (!isPojo(value)) continue;
            if (excludeTypes != null && excludeTypes.contains(value.getClass())) continue;

            result.put(entry.getKey(), value);
        }
        return result;
    }

}