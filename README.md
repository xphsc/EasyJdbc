<img src="EasyJdbc.jpg" width="40%" height="40%" />

# easyjdbc并不生产Jdbc，easyjdbc只是本地jdbc的搬运工
![Javadocs](http://www.javadoc.io/badge/cn.xphsc/easyjdbc.svg)]
[![Maven central](https://img.shields.io/maven-central/v/cn.xphsc/easyjdbc.svg)]
[![APACHE 2 License](https://img.shields.io/badge/license-Apache2-blue.svg?style=flat)](LICENSE)
#### 项目介绍
easyjdbc在spring jdbctemplate之上进行了一些包装，支持部分常用的JPA注解，使得经过注解的实体可以像Hibernate,jpa一样进行增、删、改和获取。SQL构造工具、SQL注解、链式API等让查询操作更为灵活。动态实体映射使得各种查询不再需要写大量的RowMapper。
> easyjdbc1.x 基于jdk1.7,
>easyjdbc2.x基于jdk1.8 函数表达式， SQL注解Optional返回 ,Example条件函数表达式

**功能简介：**

1：常用的JPA注解支持。

2：简化的批处理操作。

4：简化的分页操作。

5：灵活的链式查询API和SQL构造器以及SQL注解。

6：实体属性动态映射。

7：支持多种数据库（mysql,mariadb,oracle,sqlserver,postgresql,db2,sqlite,hsqldb）。

#### 安装教程
spring
~~~
 <dependency>
       <groupId>cn.xphsc</groupId>
       <artifactId>easyjdbc</artifactId>
      <version>2.1.1</version>
</dependency>
~~~
spring boot版本
~~~
 <dependency>
    <groupId>cn.xphsc.boot</groupId>
    <artifactId>easyjdbc-spring-boot-starter</artifactId>
    <version>2.1.1</version>
</dependency>
~~~

## 项目文档


#### [集成文档 - GitHub](https://github.com/xphsc/easyjdbc/wiki)
#### [更新日志 - GitHub](https://github.com/xphsc/easyjdbc/wiki/changelog)
#### [集成文档 - gitee](https://gitee.com/xphsc/easyjdbc/wikis/Home)
#### [更新日志 - gitee](https://gitee.com/xphsc/easyjdbc/wikis/changelog?sort_id=751915)
#### [QQ技术交流群：593802274]
欢迎尝试！喜欢记得star哟~
