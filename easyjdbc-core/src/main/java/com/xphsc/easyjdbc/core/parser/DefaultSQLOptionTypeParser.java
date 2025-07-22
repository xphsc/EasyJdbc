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
package com.xphsc.easyjdbc.core.parser;

import com.xphsc.easyjdbc.annotation.*;
import com.xphsc.easyjdbc.core.metadata.SQLOptionType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: 默认的SQL选项类型解析器实现类
 * 该类负责解析方法上标注的SQL相关注解，以确定方法对应的SQL命令类型
 */
public class DefaultSQLOptionTypeParser implements SQLOptionTypeParser {

    private static Set<Class<? extends Annotation>> sqlAnnotationTypes = new HashSet<Class<? extends Annotation>>();
    private static Set<Class<? extends Annotation>> sqlProviderAnnotationTypes = new HashSet<Class<? extends Annotation>>();

    static {
        //注册annotation
        sqlAnnotationTypes.add(SqlSelect.class);
        sqlAnnotationTypes.add(SqlInsert.class);
        sqlAnnotationTypes.add(SqlDelete.class);
        sqlAnnotationTypes.add(SqlUpdate.class);
        sqlProviderAnnotationTypes.add(SqlSelectProvider.class);
        sqlProviderAnnotationTypes.add(SqlUpdateProvider.class);
        sqlProviderAnnotationTypes.add(SqlInsertProvider.class);
        sqlProviderAnnotationTypes.add(SqlDeleteProvider.class);
    }


    @Override
    public SQLOptionType getSqlCommandType(Method method) {
        Class<? extends Annotation> type = getSqlAnnotationType(method);

        if (type == null) {
            type = getSqlProviderAnnotationType(method);

            if (type == null) {
                return SQLOptionType.UNKNOWN;
            }
            if (type == SqlSelectProvider.class) {
                type = SqlSelect.class;
            } else if (type == SqlInsertProvider.class) {
                type = SqlInsert.class;
            } else if (type == SqlUpdateProvider.class) {
                type = SqlUpdate.class;
            } else if (type == SqlDeleteProvider.class) {
                type = SqlDelete.class;
            }
        }

        return SQLOptionType.valueOf(type.getSimpleName().toUpperCase(Locale.ENGLISH));
    }

    private Class<? extends Annotation> getSqlAnnotationType(Method method) {
        return chooseAnnotationType(method, sqlAnnotationTypes);
    }

    private Class<? extends Annotation> getSqlProviderAnnotationType(Method method) {
        return chooseAnnotationType(method, sqlProviderAnnotationTypes);
    }

    private Class<? extends Annotation> chooseAnnotationType(Method method, Set<Class<? extends Annotation>> types) {
        for (Class<? extends Annotation> type : types) {
            Annotation annotation = method.getAnnotation(type);
            if (annotation != null) {
                return type;
            }
        }
        return null;
    }


}
