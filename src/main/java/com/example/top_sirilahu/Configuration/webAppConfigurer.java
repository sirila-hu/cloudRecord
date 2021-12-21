package com.example.top_sirilahu.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webAppConfigurer implements WebMvcConfigurer
{
    @Value("${unit.imgupload.imgUploadPath}")
    private String imgUploadPath;


    //视图控制器设置
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/recordProject/img/**")
                .addResourceLocations("file:"+imgUploadPath);
    }
}
