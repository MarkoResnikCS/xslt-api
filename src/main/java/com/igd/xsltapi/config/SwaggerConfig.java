package com.igd.xsltapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .securityContexts(Collections.singletonList(getSecurityContext()))
                .securitySchemes(Collections.singletonList(getApiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "XSLT Transformations API",
                "API used for XSLT transformations",
                "1.0.0",
                "TOC",
                getContact(),
                "License",
                "license",
                Collections.emptyList());
    }

    private Contact getContact() {
        return new Contact(
                "IGD Solutions",
                "https://igdsolutions.com",
                "info@igdsolutions.com");
    }

    private SecurityContext getSecurityContext() {
        return SecurityContext.builder().securityReferences(getSecurityReferences()).build();
    }

    private List<SecurityReference> getSecurityReferences() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
        return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiKey getApiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }
}
