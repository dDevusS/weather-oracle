package com.ddevuss.weather.oracle.location;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Slice;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

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
                                    array = @ArraySchema(schema = @Schema(implementation = LocationDto.class))
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
    ResponseEntity<List<LocationDto>> searchByName(
            @RequestParam
            @NotBlank(message = "{location.name.not.blank}")
            @Size(min = 3, message = "{location.name.size.constraint}")
            String locationName);

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
                            description = "Location with this name already exists",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
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
    ResponseEntity<Location> save(@Valid LocationDto locationDto, Principal principal);

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
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content()
                    )
            }
    )
    ResponseEntity<Void> delete(@NotNull @PositiveOrZero Long locationId);

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
    ResponseEntity<Slice<LocationDto>> get(@PositiveOrZero Integer pageNumber, Principal principal);
}
