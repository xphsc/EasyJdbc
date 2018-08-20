
package com.xphsc.easyjdbc.config;


import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * Created by ${huipei.x} on 2016/8/9.
 */

@Configuration
@EnableWebMvc
@Import({DruidAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass=true)
@ComponentScan(value="com.xphsc")
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/WEB-INF/**").addResourceLocations("classpath:/WEB-INF/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("*.html")
                .addResourceLocations("classpath:/WEB-INF/views/");
        registry.addResourceHandler("/*/*.html")
                .addResourceLocations("classpath:/WEB-INF/views/");
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index.html");
    }
}
