package com.capgemini.security4.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/web-pages/js/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/web-pages/css/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/web-pages/images/");
        registry.addResourceHandler("/web-pages/**")
                .addResourceLocations("classpath:/static/web-pages/");
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/web-pages/favicon.ico");
        registry.addResourceHandler("/pages/**")
        .addResourceLocations("classpath:static/web-pages/pages/");
    }
}
