/*
 * Copyright (c) 2018-2019 huipei.x
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
package com.xphsc.easyjdbc.boot.autoconfigure;

import com.xphsc.easyjdbc.EasyJdbcTemplate;
import com.xphsc.easyjdbc.core.binding.DaoProxy;
import com.xphsc.easyjdbc.core.factory.EasyDaoBeanFactory;
import com.xphsc.easyjdbc.core.factory.EasyDaoClassPathMapperScanner;
import com.xphsc.easyjdbc.spi.EasyJdbcInterceptor;
import com.xphsc.easyjdbc.util.Collects;
import com.xphsc.easyjdbc.util.StringUtil;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;



/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: EasyJdbc自动配置类，用于初始化EasyJdbcTemplate bean和扫描指定包下的DAO接口
 */
@ConditionalOnProperty(
        prefix = EasyJdbcProperties.EASYJDBC_PREFIX,
        name = {"enabled"},
        matchIfMissing = true
)
@Configuration
@EnableConfigurationProperties(EasyJdbcProperties.class)
@AutoConfigureAfter({JdbcTemplateAutoConfiguration.class})
public class EasyJdbcAutoConfiguration {

    private final ApplicationContext applicationContext;

    private final DataSource dataSource;

    private final EasyJdbcProperties easyJdbcProperties;

    public EasyJdbcAutoConfiguration(ApplicationContext applicationContext, DataSource dataSource,EasyJdbcProperties easyJdbcProperties) {
        this.applicationContext = applicationContext;
        this.dataSource=dataSource;
        this.easyJdbcProperties=easyJdbcProperties;
    }

    @Bean
    @ConditionalOnMissingBean(EasyJdbcTemplate.class)
    public EasyJdbcTemplate easyJdbcTemplate() {
        EasyJdbcTemplate.Builder builder = EasyJdbcTemplate.builder()
        .dataSource(this.dataSource)
        .dialectName(easyJdbcProperties.getDialect())
        .useLocalCache(easyJdbcProperties.isUseLocalCache())
        .showSQL(easyJdbcProperties.isShowSql());
        EasyJdbcTemplate easyJdbcTemplate = builder.build();
        return easyJdbcTemplate;
    }



    public static class AutoConfiguredDaoScannerRegistrar
            implements  EnvironmentAware , ImportBeanDefinitionRegistrar {
        private String[]  basePackages;

        @Override
        public void setEnvironment(Environment environment) {
            String  basePackage= environment.getProperty("easyjdbc.basePackage")!=null?
                    environment.getProperty("easyjdbc.basePackage"):
                    environment.getProperty("easyjdbc.base_package")!=null?
                    environment.getProperty("easyjdbc.base_package"):
                    environment.getProperty("easyjdbc.base-package")!=null?
                    environment.getProperty("easyjdbc.base-package"):
                    environment.getProperty("easyjdbc.basePackages")!=null?
                    environment.getProperty("easyjdbc.basePackages"):
                    environment.getProperty("easyjdbc.base-packages");
            if(StringUtil.isNotBlank(basePackage)){
                basePackages=basePackage.split(",");
            }
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            if(!Collects.isEmpty(basePackages)){
                EasyDaoClassPathMapperScanner scanner = new EasyDaoClassPathMapperScanner(registry,Repository.class);
                registerRequestProxyHandler(registry);
                scanner.doScan(basePackages);
            }



        }
        private void registerRequestProxyHandler(BeanDefinitionRegistry registry) {
            GenericBeanDefinition jdbcDaoProxyDefinition = new GenericBeanDefinition();
            jdbcDaoProxyDefinition.setBeanClass(DaoProxy.class);
            registry.registerBeanDefinition("daoProxy", jdbcDaoProxyDefinition);
        }

    }

    @Configuration
    @Import({AutoConfiguredDaoScannerRegistrar.class})
    @ConditionalOnMissingBean(value = {EasyDaoBeanFactory.class})
    public static class daoScannerRegistrarNotFoundConfiguration {
        @PostConstruct
        public  void afterPropertiesSet() {

        }


    }

}




