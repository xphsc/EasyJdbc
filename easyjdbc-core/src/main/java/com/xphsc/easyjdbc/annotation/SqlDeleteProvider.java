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
package com.xphsc.easyjdbc.annotation;

import java.lang.annotation.*;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: The annotation that specify a method that provide an SQL for deleting record(s)
 * public interface UserDao {
 *     @SqlDeleteProvider(type = SqlProvider.class, method = "deleteById")
 *     int deleteById(int id);
 *     int deleteById(Map map);
 *     public static class SqlProvider {
 *         public static String deleteById() {
 *             return "DELETE FROM users WHERE id = #{id}";
 *         }
 *     }
 * }
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SqlDeleteProvider {
    Class<?> type();

    String method();

    Class<?> returnType() default Integer.class;

}
