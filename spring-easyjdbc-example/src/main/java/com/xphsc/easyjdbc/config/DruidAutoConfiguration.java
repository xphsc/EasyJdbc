
package com.xphsc.easyjdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.xphsc.easyjdbc.EasyJdbcTemplate;

import com.xphsc.easyjdbc.core.factory.EasyDaoBeanScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author huipei.x
 * @data 创建时间 2018/6/24
 * @description 类说明 :
 */
@Configuration
public class DruidAutoConfiguration {



   @Bean
   public DataSource dataSource() {
      DruidDataSource dataSource = new DruidDataSource();
      dataSource.setUrl(DatasourceProperties.getUrl());
      dataSource.setUsername(DatasourceProperties.getUsername());
      dataSource.setPassword(DatasourceProperties.getPassword());
      if (DatasourceProperties.getInitialSize() > 0) {
         dataSource.setInitialSize(DatasourceProperties.getInitialSize());
      }
      if (DatasourceProperties.getMinIdle() > 0) {
         dataSource.setMinIdle(DatasourceProperties.getMinIdle());
      }
      if (DatasourceProperties.getMaxActive() > 0) {
         dataSource.setMaxActive(DatasourceProperties.getMaxActive());
      }
      dataSource.setTestOnBorrow(DatasourceProperties.isTestOnBorrow());
      try {
         dataSource.init();
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
      return dataSource;
   }

  @Bean("easyJdbcTemplate")
   public EasyJdbcTemplate easyJdbcTemplate() {
      EasyJdbcTemplate easyJdbcTemplate=new EasyJdbcTemplate();
      easyJdbcTemplate.setDataSource(dataSource());
      easyJdbcTemplate.setDialectName("mysql");
     easyJdbcTemplate.setJdbcTemplate(jdbcTemplate());
      return  easyJdbcTemplate;
   }

   @Bean
   public JdbcTemplate jdbcTemplate(){
      JdbcTemplate jdbcTemplate=new JdbcTemplate();
      jdbcTemplate.setDataSource(dataSource());
      return jdbcTemplate;
   }

   @Bean
   public EasyDaoBeanScannerConfigurer easyDaoBeanScannerConfigurer(){
       EasyDaoBeanScannerConfigurer daoBeanScannerConfigurer=new EasyDaoBeanScannerConfigurer();
       /**
        * dao扫描路径,配置符合spring方式 只需设置能扫描到包名或是dao的包名全路径
        * daoBeanScannerConfigurer.setBasePackage("com.xphsc);
        */
       daoBeanScannerConfigurer.setBasePackage("com.xphsc.easyjdbc.dao");
       /**
        * 使用的注解,默认是@EasyDao 设置能使用@Repository注解,推荐@Repository
        */
       daoBeanScannerConfigurer.setAnnotation(Repository.class);
       return daoBeanScannerConfigurer;
   }
}

