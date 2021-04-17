/*
 * Copyright (c) 2021  huipei.x
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
package com.xphsc.easyjdbc.core.metadata.type;

import com.xphsc.easyjdbc.core.exception.EasyJdbcException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * {@link Type}
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description:
 * @since 2.1.0
 */
public abstract class TypeReflect<T> {
    private final Type rawType = this.getSuperclassTypeParameter(this.getClass());

    protected TypeReflect() {
    }

    Type getSuperclassTypeParameter(Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            if (TypeReflect.class != genericSuperclass) {
                return this.getSuperclassTypeParameter(clazz.getSuperclass());
            } else {
                throw new EasyJdbcException("'" + this.getClass() + "' extends TypeReference but misses the type parameter. Remove the extension or add a type parameter to it.");
            }
        } else {
            Type rawType = ((ParameterizedType)genericSuperclass).getActualTypeArguments()[0];
            if (rawType instanceof ParameterizedType) {
                rawType = ((ParameterizedType)rawType).getRawType();
            }

            return rawType;
        }
    }

    public final Type getRawType() {
        return this.rawType;
    }

    @Override
    public String toString() {
        return this.rawType.toString();
    }
}
