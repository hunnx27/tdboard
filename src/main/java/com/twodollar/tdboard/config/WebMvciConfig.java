package com.twodollar.tdboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvciConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String UPLOAD_DIR;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // addResourceLocations 등록 시 어떤 방식으로 접근하는지(file or classpath) 설정
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+UPLOAD_DIR + "/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
