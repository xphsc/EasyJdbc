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

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: 注解用于标记数据访问层（Data Access Object）的组件类。
 * 它继承了 {@code Component} 注解，使得被 {@code Dao} 注解的类自动成为 Spring 框架管理的 Bean。
 * {@code Dao} 注解的应用旨在通过约定优于配置的原则，简化数据访问层的开发。
 * 它提供了一种标准化的方式来标识 DAO 类，而无需手动在 XML 或配置类
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Dao {
    String value() default "";
}
