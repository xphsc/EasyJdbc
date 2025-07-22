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
 * @description: The annotation that specify a method that provide an SQL for retrieving record(s).
 * For reference, the example
 * public interface UserDao  {
 *     @SqlSelectProvider(type = SqlProvider.class, method = "findById")
 *     User findById(Map map);
 *     @since 2.1.1
 *     User findById(@SqlParam("id") int id);
 *     User findById(@SqlParam("user") User user);
 *     public static class SqlProvider {
 *         public static String findById() {
 *             return "SELECT id, name FROM users WHERE id = #{id}";
 *         }
 *          @since 2.1.1
 *          public static String findById(User user) {
 *            return "SELECT id, name FROM users WHERE id = #{user.id}";
 *         }
 *     }
 * }
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SqlSelectProvider {
    Class<?> type();

    String method();

    Class<?> entityClass() default void.class;
}
