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
package com.xphsc.easyjdbc.core.factory;


import com.xphsc.easyjdbc.annotation.Dao;
import com.xphsc.easyjdbc.core.binding.DaoProxy;
import com.xphsc.easyjdbc.util.StringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import java.lang.annotation.Annotation;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description:  EasyDaoBeanScannerConfigurer class implements BeanDefinitionRegistryPostProcessor for scanning and registering Dao layer interfaces
 * This class is mainly used to automatically scan the specified package path for Dao layer interfaces annotated with @Dao, and register them as Bean definitions
 */
public class EasyDaoBeanScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    private String basePackage;

    private Class<? extends Annotation> annotation = Dao.class;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        /**
         * Registered Agent Class
         */
        registerRequestProxyHandler(registry);

        EasyDaoClassPathMapperScanner scanner = new EasyDaoClassPathMapperScanner(registry, annotation);
        /**
         * Loading Dao Layer Interface
         */
        if (StringUtil.isNotBlank(this.basePackage)) {
            scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    /**
     * RequestProxyHandler Manual registration of proxy classes eliminates the trouble of user configuring XML
     *
     * @param registry
     */
    private void registerRequestProxyHandler(BeanDefinitionRegistry registry) {
        GenericBeanDefinition jdbcDaoProxyDefinition = new GenericBeanDefinition();
        jdbcDaoProxyDefinition.setBeanClass(DaoProxy.class);
        registry.registerBeanDefinition("daoProxy", jdbcDaoProxyDefinition);
    }

    public void setAnnotation(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }




}

