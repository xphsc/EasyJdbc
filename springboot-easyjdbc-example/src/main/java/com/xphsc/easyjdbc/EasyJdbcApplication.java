package com.xphsc.easyjdbc;



import com.xphsc.easyjdbc.springboot.annotation.EnableEasyJdbc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author huipei.x
 * @data 创建时间 2018/6/24
 * @description 类说明 :
 */
@SpringBootApplication
@EnableEasyJdbc
public class EasyJdbcApplication {
    public static void main(String[] args) {

        SpringApplication.run(EasyJdbcApplication.class, args);
    }
}
