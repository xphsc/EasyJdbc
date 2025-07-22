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
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import static org.springframework.beans.factory.support.AbstractBeanDefinition.AUTOWIRE_NO;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: A custom scanner that extends ClassPathBeanDefinitionScanner, specifically for scanning and registering bean definitions of Dao interfaces.
 * This class is primarily responsible for scanning specified packages to find interfaces annotated with @Dao or @Repository,
 * and registers them as bean definitions in the Spring application context.
 */
public class EasyDaoClassPathMapperScanner extends ClassPathBeanDefinitionScanner {


    public EasyDaoClassPathMapperScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annotation) {

        super(registry, false);
        addIncludeFilter(new AnnotationTypeFilter(annotation));
        if (!Dao.class.equals(annotation)) {
            addIncludeFilter(new AnnotationTypeFilter(Dao.class));
        }
        if (!Repository.class.equals(annotation)) {
            addIncludeFilter(new AnnotationTypeFilter(Repository.class));
        }
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = null;
        if (basePackages != null) {
            beanDefinitions = super.doScan(basePackages);
        }

        if (beanDefinitions.isEmpty()) {
            logger.warn("No Dao interface was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        }
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definition.getPropertyValues().add("proxy", getRegistry().getBeanDefinition("daoProxy"));
            definition.getPropertyValues().add("daoInterface", definition.getBeanClassName());
            definition.setBeanClass(EasyDaoBeanFactory.class);
        }

        return beanDefinitions;
    }

    /**
     * The default is Scan@Component, so all of the @Component annotations will be Scan.
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        if (beanDefinition.getMetadata() != null) {
            return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
        }
        return false;
    }

}

