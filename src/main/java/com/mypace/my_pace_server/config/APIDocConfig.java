package com.mypace.my_pace_server.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Auth Application build by Aditya Methe",
            description = "Generic auth app that can be used with any application.",
            contact = @Contact(name = "Aditya Methe", url = "", email = "metheaditya9@gmail.com"),
            version = "1.0.0",
            summary =
                "This is a common authentication application which can be used in any application."),
    security = {@SecurityRequirement(name = "bearerAuth")})
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer", // Authorization: Bearer token
    bearerFormat = "JWT")
public class APIDocConfig {}
