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
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.util.Collects;
import com.xphsc.easyjdbc.util.ObjectExtractors;
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
import java.util.function.Consumer;


/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: AnnotationMethodProcessor类继承自AbstractDaoMethodProcessor
 * 用于处理注解定义的DAO方法
 */
public class AnnotationMethodProcessor extends AbstractDaoMethodProcessor {

    private Method providerMethod;
    private Class<?> providerType;
    private String sql = "";
    private String providerMethodName = null;
    private boolean useGeneratedKeys = false;
    private String keyProperty = null;
    private String keyColumn = null;
    private boolean hasInsertOrUpdatePlaceHolder = false;
    private Class<?>[] providerMethodParameterTypes;

    @Override
    public Object process() {
        Object[] result = null;
        getAnnotationType();
        SQLOptionTypeParser sqlOptionTypeParser
                = new DefaultSQLOptionTypeParser();
        SQLOptionType sqlOptionType
                = sqlOptionTypeParser.getSqlCommandType(method);
        if (providerType != null) {
            for (Method m : this.providerType.getMethods()) {
                if (providerMethodName.equals(m.getName())) {
                    if (m.getReturnType() == String.class) {
                        if (providerMethod != null) {
                            throw new EasyJdbcException("Error creating Sql for SqlProvider. Method '"
                                    + providerMethodName + "' is found multiple in SqlProvider '" + this.providerType.getName()
                                    + "'. Sql provider method can not overload.");
                        }
                        this.providerMethod = m;
                    }
                }
            }
            this.providerMethodParameterTypes = this.providerMethod.getParameterTypes();
            try {
                if (!Collects.isEmpty(providerMethodParameterTypes)) {
                    Class<?> parameterType = providerMethodParameterTypes[0];
                    if (parameterType.isAssignableFrom(Map.class)) {
                        sql = (String) providerMethod.invoke(providerType.newInstance(), paramsMap);
                    } else if (ObjectExtractors.isPojo(parameterType)) {
                        switch (providerMethodParameterTypes.length) {
                            case 1:
                                Set<Class<?>> exclude = new HashSet<>();
                                exclude.add(PageInfo.class);
                                Object target = ObjectExtractors.extractFirstPojo(paramsMap, exclude, Object.class);
                                sql = (String) providerMethod.invoke(providerType.newInstance(), target);
                                break;
                            case 2:
                                Object target1 = ObjectExtractors.extractFirstPojo(paramsMap, null, providerMethodParameterTypes[0]);
                                Object target2 = ObjectExtractors.extractFirstPojo(paramsMap, null, providerMethodParameterTypes[1]);
                                sql = (String) providerMethod.invoke(providerType.newInstance(), target1, target2);
                                break;
                        }
                    } else {
                        List objects = ObjectExtractors.extractAllByType(paramsMap, parameterType);
                        sql = (String) providerMethod.invoke(providerType.newInstance(), objects.toArray());
                    }
                } else {
                    sql = (String) providerMethod.invoke(providerType.newInstance());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (sqlOptionType.equals(SQLOptionType.SQLSELECT)) {
            SQLSelectParser sqlSelectParser = new DefaultSQLSelectParser();
            return sqlSelectParser.select(
                    sql,
                    simpleJdbcDao,
                    persistentClass,
                    method,
                    paramsMap
            );

        }
        SQLParser sqlParser = new DefaultSQLParser();
        if (hasInsertOrUpdatePlaceHolder) {
            result = sqlParser.sqlPlaceHolder(sql, paramsMap, false);
        } else if (sqlParser.hasOgnlPlaceHolder(sql)) {
            result = sqlParser.sqlPlaceHolder(sql, paramsMap, true);
        } else {
            result = sqlParser.sqlPlaceHolder(sql, null, true);
        }
        Object returnResult = null;
        Class<?> returnType = method.getReturnType();
        boolean returnsOptional = Optional.class.equals(returnType);
        if (sqlOptionType.equals(SQLOptionType.SQLUPDATE)) {
            if (hasInsertOrUpdatePlaceHolder) {
                String sql = (String) result[0];
                returnResult = Collects.isNotEmpty(paramsMap) ? simpleJdbcDao.getEasyJdbcTemplate().getJdbcBuilder().update((String) result[0], (Object[]) result[1]) :
                        simpleJdbcDao.getEasyJdbcTemplate().getJdbcBuilder().update(sql);
            }
            if (returnsOptional) {
                return Optional.ofNullable(returnResult);
            } else {
                return returnResult instanceof int[] ? ((int[]) returnResult).length : returnResult;
            }
        }
        if (sqlOptionType.equals(SQLOptionType.SQLINSERT) || sqlOptionType.equals(SQLOptionType.SQLDELETE)) {
            if (useGeneratedKeys) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                final String finalKeyProperty = keyProperty;
                final String finalKeyColumn = keyColumn;
                final Object[] finalResult = result;
                final Object[] finalResult1 = result;
                simpleJdbcDao.getEasyJdbcTemplate().getJdbcBuilder().update(
                        new PreparedStatementCreator() {
                            @Override
                            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                                PreparedStatement ps=null;
                                if(StringUtil.isNotBlank(finalKeyColumn)){
                                     ps = con.prepareStatement((String) finalResult[0],new String[]{finalKeyColumn});
                                }
                                if(StringUtil.isNotBlank(finalKeyProperty)){
                                    ps = con.prepareStatement((String) finalResult[0],new String[]{finalKeyProperty});
                                }
                                int i = 1;
                                for (Object object : (Object[]) finalResult1[1]) {
                                    ps.setObject(i, object);
                                    i++;
                                }
                                return ps;
                            }
                        },
                        keyHolder);
                returnResult = getKey(method, keyHolder);


            } else {
                if (hasInsertOrUpdatePlaceHolder) {
                    returnResult = simpleJdbcDao.getEasyJdbcTemplate().getJdbcBuilder().update((String) result[0], (Object[]) result[1]);
                }
            }
        }
        if (returnsOptional) {
            return Optional.ofNullable(returnResult);
        } else {
            return returnResult;
        }
    }

    private Object getKey(Method method, KeyHolder keyHolder) {
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

    private void getAnnotationType() {
        if (annotation != null) {
            processAnnotation((Annotation) annotation);
        }
        for (Annotation ann : method.getAnnotations()) {
            if (ann.annotationType().equals(SqlOptions.class)) {
                SqlOptions sqlOptions = (SqlOptions) ann;
                useGeneratedKeys = sqlOptions.useGeneratedKeys();
                keyProperty = StringUtil.isNotBlank(sqlOptions.keyProperty()) ? sqlOptions.keyProperty() :"";
                keyColumn = StringUtil.isNotBlank(sqlOptions.keyColumn()) ? sqlOptions.keyColumn() : "";
            }
        }
    }
    private void processAnnotation(Annotation ann) {
        Map<Class<? extends Annotation>, Consumer<Annotation>> handlers = new HashMap<Class<? extends Annotation>, Consumer<Annotation>>() {{
            put(SqlInsert.class, a -> {
                hasInsertOrUpdatePlaceHolder = true;
                sql = ((SqlInsert) a).value();
            });
            put(SqlUpdate.class, a -> {
                hasInsertOrUpdatePlaceHolder = true;
                sql = ((SqlUpdate) a).value();
            });
            put(SqlDelete.class, a -> {
                hasInsertOrUpdatePlaceHolder = true;
                sql = ((SqlDelete) a).value();
            });
            put(SqlUpdateProvider.class, a -> {
                hasInsertOrUpdatePlaceHolder = true;
                SqlUpdateProvider p = (SqlUpdateProvider) a;
                providerType = p.type();
                providerMethodName = p.method();
                if (p.returnType() != null) {
                    persistentClass = p.returnType();
                }
            });
            put(SqlInsertProvider.class, a -> {
                hasInsertOrUpdatePlaceHolder = true;
                SqlInsertProvider p = (SqlInsertProvider) a;
                providerType = p.type();
                providerMethodName = p.method();
                if (p.returnType() != null) {
                    persistentClass = p.returnType();
                }
            });
            put(SqlDeleteProvider.class, a -> {
                hasInsertOrUpdatePlaceHolder = true;
                SqlDeleteProvider p = (SqlDeleteProvider) a;
                providerType = p.type();
                providerMethodName = p.method();
                if (p.returnType() != null) {
                    persistentClass = p.returnType();
                }
            });
            put(SqlSelect.class, a -> {
                SqlSelect p = (SqlSelect) a;
                sql = p.value();
                if (p.entityClass() != null && p.entityClass() != void.class) {
                    persistentClass = p.entityClass();
                }
            });
            put(SqlSelectProvider.class, a -> {
                SqlSelectProvider p = (SqlSelectProvider) a;
                providerType = p.type();
                providerMethodName = p.method();
                if (p.entityClass() != null && p.entityClass() != void.class) {
                    persistentClass = p.entityClass();
                }
            });
        }};
        Consumer<Annotation> handler = handlers.get(ann.annotationType());
        if (handler != null) {
            handler.accept(ann);
        }
    }
    public static class ReturnKeyType {

        public enum Type {
            INTEGER, LONG, SHORT, DOUBLE, BYTE, OBJECT, UNKNOWN
        }

        private final Type type;

        public ReturnKeyType(Method method) {
            this.type = detectType(method.getReturnType());
        }

        private Type detectType(Class<?> returnType) {
            if (returnType == int.class || returnType == Integer.class) return Type.INTEGER;
            if (returnType == long.class || returnType == Long.class) return Type.LONG;
            if (returnType == short.class || returnType == Short.class) return Type.SHORT;
            if (returnType == double.class || returnType == Double.class) return Type.DOUBLE;
            if (returnType == byte.class || returnType == Byte.class) return Type.BYTE;
            if (Object.class.isAssignableFrom(returnType)) return Type.OBJECT;
            return Type.UNKNOWN;
        }

        public Type getType() {
            return type;
        }

        public boolean isInteger() {
            return type == Type.INTEGER;
        }

        public boolean isLong() {
            return type == Type.LONG;
        }

        public boolean isShort() {
            return type == Type.SHORT;
        }

        public boolean isDouble() {
            return type == Type.DOUBLE;
        }

        public boolean isByte() {
            return type == Type.BYTE;
        }

        public boolean isObject() {
            return type == Type.OBJECT;
        }

        public boolean isUnknown() {
            return type == Type.UNKNOWN;
        }
    }
}

