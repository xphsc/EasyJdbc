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
package com.xphsc.easyjdbc.core.factory;


import com.xphsc.easyjdbc.annotation.DaoScan;
import com.xphsc.easyjdbc.core.binding.DaoProxy;
import com.xphsc.easyjdbc.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.factory.support.AbstractBeanDefinition.AUTOWIRE_NO;

/**
 * {@link DaoScannerRegistrar }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description:  Use this annotation daoscan to register the easyjddbc mapper interface when Java
 * @since 2.0.8
 */
public class DaoScannerRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerRequestProxyHandler(registry);
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(DaoScan.class.getName()));


        Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
        EasyDaoClassPathMapperScanner scanner = new EasyDaoClassPathMapperScanner(registry,annotationClass);
        Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
        }

        List<String> basePackages = new ArrayList<String>();
        for (String pkg : annoAttrs.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : annoAttrs.getStringArray("basePackages")) {
            if (StringUtil.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : annoAttrs.getClassArray("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }
    private void registerRequestProxyHandler(BeanDefinitionRegistry registry) {
        GenericBeanDefinition jdbcDaoProxyDefinition = new GenericBeanDefinition();
        jdbcDaoProxyDefinition.setBeanClass(DaoProxy.class);
        jdbcDaoProxyDefinition.setAutowireMode(AUTOWIRE_NO);
        registry.registerBeanDefinition("daoProxy", jdbcDaoProxyDefinition);
    }

}
