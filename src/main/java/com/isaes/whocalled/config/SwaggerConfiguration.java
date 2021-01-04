package com.isaes.whocalled.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;


import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
//@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket postsApi() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("WhoCalled")
                .description("Demo who called service")
                .contact(new Contact("IsaEs","http://isaes.com.tr","merhaba@isaes.com.tr")).license("MIT")
                .version("1.0").build();

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("public-api")
                .apiInfo(apiInfo)
                .securityContexts(Collections.singletonList(SecurityContext.builder().securityReferences(defaultAuth()).build()))
                .securitySchemes(Collections.singletonList(new ApiKey("JWT", "Authorization", "header")))
                .select()
                .paths(regex("/api/.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
    }

}
