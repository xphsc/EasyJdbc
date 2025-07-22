/*
 * Copyright (c) 2020 huipei.x
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

import com.xphsc.easyjdbc.core.factory.DaoScannerRegistrar;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * {@link DaoScannerRegistrar }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description:  Use this annotation daoScan to register the easyjddbc mapper interface when Java
 * @since 2.0.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DaoScannerRegistrar.class)
public @interface DaoScan {

    String[] value() default {};


    String basePackages() default "";


    Class<?>[] basePackageClasses() default {};


    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;


    Class<? extends Annotation> annotationClass() default Dao.class ;


    Class<?> daoInterface() default Class.class;




}
