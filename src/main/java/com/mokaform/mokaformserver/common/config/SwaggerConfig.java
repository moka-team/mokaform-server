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
                .description("Team MOKA의 mokaform 웹 애플리케이션 API입니다.\n" +
                        "로그인 요청 후에 응답으로 온 accessToken을 오른쪽 Authorize 버튼을 누르고 입력한 후에 API 요청을 보내면" +
                        " 인가가 필요한 요청에 대한 응답을 받을 수 있습니다.");

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
