package com.ddevuss.weather.oracle.configuration;

import com.ddevuss.weather.oracle.dto.AccessTokenDto;
import com.ddevuss.weather.oracle.dto.ForecastDto;
import com.ddevuss.weather.oracle.dto.LocationDto;
import com.ddevuss.weather.oracle.dto.RefreshTokenDto;
import com.ddevuss.weather.oracle.dto.UserDto;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;

import java.util.List;
import java.util.Map;

@Configuration
public class SwaggerConf {

    @Bean
    public OpenAPI customOpenAPI() {
        Components components = new Components();

        Class<?>[] classes = {
                UserDto.class,
                ProblemDetail.class,
                LocationDto.class,
                AccessTokenDto.class,
                ForecastDto.class,
                RefreshTokenDto.class
        };

        for (Class<?> clazz : classes) {
            Map<String, Schema> schemas = ModelConverters.getInstance().read(clazz);
            schemas.forEach(components::addSchemas);
        }

        components.addResponses("BadRequest", new ApiResponse()
                        .description("Bad request")
                        .content(
                                new Content()
                                        .addMediaType(
                                                "application/json",
                                                new MediaType().schema(
                                                        new Schema<>()
                                                                .$ref("#/components/schemas/ProblemDetail")
                                                )
                                        )
                        )
                )
                .addResponses("Unauthorized", new ApiResponse()
                        .description("Unauthorized")
                        .content(
                                new Content()
                                        .addMediaType(
                                                "application/json",
                                                new MediaType().schema(
                                                        new Schema<>()
                                                                .$ref("#/components/schemas/ProblemDetail")
                                                )
                                        )
                        )
                )
                .addSecuritySchemes("bearer",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Cookie with access token")

                )
                .addSecuritySchemes("refreshToken",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .scheme("refreshToken")
                                .bearerFormat("JWT")
                                .description("Http only cookie with refresh token")
                );

        return new OpenAPI()
                .info(new Info()
                        .title("Weather API")
                        .description("Weather API"))
                .tags(List.of(
                        new Tag().name("Authentication").description("Authentication and registration users"),
                        new Tag().name("Forecasts"),
                        new Tag().name("Locations")
                ))
                .components(components);
    }
}
