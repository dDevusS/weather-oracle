package com.ddevuss.weather.oracle.controller.api.docs;

import com.ddevuss.weather.oracle.dto.ApiErrorDto;
import com.ddevuss.weather.oracle.dto.JwtResponseDto;
import com.ddevuss.weather.oracle.dto.RefreshTokenDto;
import com.ddevuss.weather.oracle.dto.UserCreateDto;
import com.ddevuss.weather.oracle.dto.UserReadDto;
import com.ddevuss.weather.oracle.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication")
public interface UserAuthController {

    @Operation(
            summary = "Login for users",
            description = "Required login and password",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Return access and refresh tokens",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponseDto.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            ref = "#/components/responses/BadRequest"),
                    @ApiResponse(
                            responseCode = "401",
                            ref = "#/components/responses/Unauthorized")
            }
    )
    ResponseEntity<?> login(User user);

    @Operation(
            summary = "Registration for users",
            description = "Registration with login and password",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Success registration of user",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserReadDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ApiErrorDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User with this login already exists",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiErrorDto.class)
                            )
                    )
            }
    )
    ResponseEntity<?> registration(UserCreateDto user);

    @Operation(
            summary = "Refresh token",
            description = "Refresh an access token and a refresh token with a validated refresh token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success refresh tokens",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            ref = "#/components/responses/BadRequest"),
                    @ApiResponse(
                            responseCode = "401",
                            ref = "#/components/responses/Unauthorized")
            }
    )
    ResponseEntity<?> refresh(RefreshTokenDto jsonRefreshToken);

    @Operation(hidden = true)
    ResponseEntity<?> test();
}
