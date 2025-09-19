package com.ddevuss.weather.oracle.controller.api.docs;

import com.ddevuss.weather.oracle.dto.ForecastDto;
import com.ddevuss.weather.oracle.dto.LocationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Forecasts")
public interface ForecastController {

    @Operation(
            tags = {"/api/forecast"},
            summary = "Get list of forecasts",
            description = "Get forecasts from external API server",
            security = @SecurityRequirement(name = "bearer"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ForecastDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            ref = "#/components/responses/BadRequest"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            ref = "#/components/responses/Unauthorized"
                    )
            }
    )
    ResponseEntity<List<ForecastDto>> getWeatherForecast(List<@Valid @NotNull LocationDto> locations);
}
