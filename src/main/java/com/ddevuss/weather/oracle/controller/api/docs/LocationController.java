package com.ddevuss.weather.oracle.controller.api.docs;

import com.ddevuss.weather.oracle.dto.ApiErrorDto;
import com.ddevuss.weather.oracle.dto.api.LocationApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

@Tag(name = "Locations")
public interface LocationController {

    @Operation(
            summary = "Get locations API server",
            description = "Get list of locations from external API server",
            security = @SecurityRequirement(name = "bearer"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = LocationApiResponseDto.class))
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
    ResponseEntity<?> searchLocationsByName(String locationName);

    @Operation(
            summary = "Save location",
            description = "Save location for the user",
            security = @SecurityRequirement(name = "bearer"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiErrorDto.class)
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
    ResponseEntity<?> saveLocation(LocationApiResponseDto locationApiResponseDto, Principal principal);

    @Operation(
            summary = "Delete location",
            description = "Delete location from user with 'id'",
            security = @SecurityRequirement(name = "bearer"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            ref = "#/components/responses/BadRequest"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            ref = "#/components/responses/Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content()
                    )
            }
    )
    ResponseEntity<?> deleteLocation(Long locationId);

    @Operation(
            summary = "Get user's locations",
            description = "Get slice of locations for user",
            security = @SecurityRequirement(name = "bearer"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SliceOfLocations.class)
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
    ResponseEntity<?> getLocations(Integer pageNumber, Principal principal);
}
