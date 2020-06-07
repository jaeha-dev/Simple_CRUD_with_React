package com.github.devsjh.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

/**
 * @title  : Open API 설정 클래스
 * @author : jaeha-dev (Git)
 */
@OpenAPIDefinition(
        info = @Info(
            title = "Simple CRUD App (API 명세)",
            description = "Spring Doc - Simple CRUD App (API 명세)",
            version = "v1",
            contact = @Contact(name = "devsjh", email = "jaeha.dev@gmail.com"),
            license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        )
)
@Configuration
public class OpenApiConfig {
    // https://webgori.github.io/spring/2020/02/09/OpenAPI-3.0을-이용한-Spring-REST-API-문서화.html
    // https://springdoc.org
}