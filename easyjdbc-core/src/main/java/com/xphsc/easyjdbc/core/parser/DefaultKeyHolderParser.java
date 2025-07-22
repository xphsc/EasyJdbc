package com.xphsc.easyjdbc.core.parser;

import com.xphsc.easyjdbc.core.processor.AnnotationMethodProcessor;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.Method;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: DefaultKeyHolderParser实现了KeyHolderParser接口，用于解析获取主键Key
 */
public class DefaultKeyHolderParser implements  KeyHolderParser {
    @Override
    public Object getKey(Method method, KeyHolder keyHolder) {
       ReturnKeyType keyType = new ReturnKeyType(method);
        Number rawKey = keyHolder.getKey(); // 只取一次

        switch (keyType.getType()) {
            case INTEGER:
                return rawKey.intValue();
            case LONG:
                return rawKey.longValue();
            case SHORT:
                return rawKey.shortValue();
            case DOUBLE:
                return rawKey.doubleValue();
            case BYTE:
                return rawKey.byteValue();
            case OBJECT:
                return rawKey;
            default:
                return rawKey;
        }
    }

    @Override
    public Object getKey(Class<?> type, KeyHolder keyHolder) {
        ReturnKeyType keyType = new ReturnKeyType(type);
        Number rawKey = keyHolder.getKey(); // 只取一次

        switch (keyType.getType()) {
            case INTEGER:
                return rawKey.intValue();
            case LONG:
                return rawKey.longValue();
            case SHORT:
                return rawKey.shortValue();
            case DOUBLE:
                return rawKey.doubleValue();
            case BYTE:
                return rawKey.byteValue();
            case OBJECT:
                return rawKey;
            default:
                return rawKey;
        }
    }

    @Override
    public Object getKey(Method method, Number result) {
        ReturnKeyType keyType = new ReturnKeyType(method);
        switch (keyType.getType()) {
            case INTEGER:
                return result.intValue();
            case LONG:
                return result.longValue();
            case SHORT:
                return result.shortValue();
            case DOUBLE:
                return result.doubleValue();
            case BYTE:
                return result.byteValue();
            case OBJECT:
                return result;
            default:
                return result;
        }
    }


    public static class ReturnKeyType {

        public enum Type {
            INTEGER, LONG, SHORT, DOUBLE, BYTE, OBJECT,STRING, UNKNOWN
        }

        private final AnnotationMethodProcessor.ReturnKeyType.Type type;

        public ReturnKeyType(Method method) {
            this.type = detectType(method.getReturnType());
        }
        public ReturnKeyType(Class<?> type) {
            this.type = detectType(type);
        }
        private AnnotationMethodProcessor.ReturnKeyType.Type detectType(Class<?> returnType) {
            if (returnType == int.class || returnType == Integer.class) return AnnotationMethodProcessor.ReturnKeyType.Type.INTEGER;
            if (returnType == long.class || returnType == Long.class) return AnnotationMethodProcessor.ReturnKeyType.Type.LONG;
            if (returnType == short.class || returnType == Short.class) return AnnotationMethodProcessor.ReturnKeyType.Type.SHORT;
            if (returnType == double.class || returnType == Double.class) return AnnotationMethodProcessor.ReturnKeyType.Type.DOUBLE;
            if (returnType == byte.class || returnType == Byte.class) return AnnotationMethodProcessor.ReturnKeyType.Type.BYTE;
            if (Object.class.isAssignableFrom(returnType)) return AnnotationMethodProcessor.ReturnKeyType.Type.OBJECT;
            return AnnotationMethodProcessor.ReturnKeyType.Type.UNKNOWN;
        }

        public AnnotationMethodProcessor.ReturnKeyType.Type getType() {
            return type;
        }

        public boolean isInteger() {
            return type == AnnotationMethodProcessor.ReturnKeyType.Type.INTEGER;
        }

        public boolean isLong() {
            return type == AnnotationMethodProcessor.ReturnKeyType.Type.LONG;
        }

        public boolean isShort() {
            return type == AnnotationMethodProcessor.ReturnKeyType.Type.SHORT;
        }

        public boolean isDouble() {
            return type == AnnotationMethodProcessor.ReturnKeyType.Type.DOUBLE;
        }

        public boolean isByte() {
            return type == AnnotationMethodProcessor.ReturnKeyType.Type.BYTE;
        }

        public boolean isObject() {
            return type == AnnotationMethodProcessor.ReturnKeyType.Type.OBJECT;
        }

        public boolean isUnknown() {
            return type == AnnotationMethodProcessor.ReturnKeyType.Type.UNKNOWN;
        }
    }
}
