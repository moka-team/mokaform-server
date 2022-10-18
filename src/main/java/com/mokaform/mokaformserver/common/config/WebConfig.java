package com.mokaform.mokaformserver.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedOrigins("http://localhost:3000", "https://mokaform-client-q6w1.vercel.app")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
