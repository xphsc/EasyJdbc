/*
 * Copyright (c) 2019 huipei.x
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
package com.xphsc.easyjdbc.core.lambda;

import com.xphsc.easyjdbc.core.exception.EasyJdbcException;
import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author huipei.x
 * @date  2019-3-20
 * @description
 */
public class Reflections {
    private static final Pattern GET_PATTERN = Pattern.compile("^get[A-Z].*");
    private static final Pattern IS_PATTERN = Pattern.compile("^is[A-Z].*");

    private Reflections() {
    }

    public static String fieldNameForLambdaFunction(LambdaFunction fn) {
        try {
            Method e = fn.getClass().getDeclaredMethod("writeReplace", new Class[0]);
            e.setAccessible(Boolean.TRUE.booleanValue());
            SerializedLambda serializedLambda = (SerializedLambda)e.invoke(fn, new Object[0]);
            String getter = serializedLambda.getImplMethodName();
            if(GET_PATTERN.matcher(getter).matches()) {
                getter = getter.substring(3);
            } else if(IS_PATTERN.matcher(getter).matches()) {
                getter = getter.substring(2);
            }

            return Introspector.decapitalize(getter);
        } catch (ReflectiveOperationException var) {
            throw new EasyJdbcException(var);
        }
    }
    public static List<String> fieldNameForLambdaFunction(LambdaFunction... fn) {
        LinkedList<String> fieldName=new LinkedList<>();
        for(LambdaFunction lambdaFunction:fn){
            fieldName.add(fieldNameForLambdaFunction(lambdaFunction)) ;
        }
      return fieldName;
    }

    public static <T> T classForLambdaSupplier(LambdaSupplier supplier) {
        return (supplier != null ? (T) supplier.get() : null);
    }
}
