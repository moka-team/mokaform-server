package com.mokaform.mokaformserver.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "https://mokaform-client-q6w1.vercel.app",
                        "https://startling-melba-4ca4af.netlify.app",
                        "https://feat-deploy--papaya-horse-fada9b.netlify.app",
                        "https://mokaform-client-bnjs9ux4o-mokaform-test.vercel.app",
                        "https://mokaform-client.vercel.app",
                        "https://mokaform.netlify.app",
                        "https://www.mokaform.site",
                        "https://test.mokaform.site")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }
}
