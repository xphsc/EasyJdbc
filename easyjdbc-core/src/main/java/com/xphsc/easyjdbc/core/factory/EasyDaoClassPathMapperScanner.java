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

import com.xphsc.easyjdbc.annotation.EasyDao;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * @author huipei.x
 * @date  2018-8-20
 * @description  :
 */
public class EasyDaoClassPathMapperScanner extends ClassPathBeanDefinitionScanner {


    public EasyDaoClassPathMapperScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annotation) {

        super(registry, false);
        addIncludeFilter(new AnnotationTypeFilter(annotation));
        if (!EasyDao.class.equals(annotation)) {
            addIncludeFilter(new AnnotationTypeFilter(EasyDao.class));
        }
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions=null;
        if(basePackages !=null){
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
     * 默认会Scan @Component 这样所以的被@Component 注解的都会Scan
     */
   @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        if(beanDefinition.getMetadata()!=null){
            return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
        }
      return false;
    }

}

