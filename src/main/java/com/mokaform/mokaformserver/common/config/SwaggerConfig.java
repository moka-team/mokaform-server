package com.mokaform.mokaformserver.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        Info info = new Info().title("Demo API").version(appVersion)
                .description("Team MOKA의 mokaform 웹 애플리케이션 API입니다.");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("JWT", getAuthScheme()))
                .addSecurityItem(getSecurityItem())
                .info(info);
    }

    private SecurityScheme getAuthScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("accessToken");
    }

    private SecurityRequirement getSecurityItem() {
        SecurityRequirement securityItem = new SecurityRequirement();
        securityItem.addList("JWT");
        return securityItem;
    }
}
