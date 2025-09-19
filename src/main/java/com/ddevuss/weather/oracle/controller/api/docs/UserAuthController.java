package com.ddevuss.weather.oracle.controller.api.docs;

import com.ddevuss.weather.oracle.dto.AccessTokenDto;
import com.ddevuss.weather.oracle.dto.UserDto;
import com.ddevuss.weather.oracle.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.Map;

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
                                    schema = @Schema(implementation = AccessTokenDto.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            ref = "#/components/responses/BadRequest"),
                    @ApiResponse(
                            responseCode = "401",
                            ref = "#/components/responses/Unauthorized")
            }
    )
    ResponseEntity<AccessTokenDto> login(User user);

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
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            ref = "#/components/responses/BadRequest"),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User with this login already exists",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    )
            }
    )
    ResponseEntity<UserDto> registration(UserDto user);

    @Operation(
            summary = "Refresh token",
            description = "Refresh an access token and a refresh token with a validated refresh token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success refresh tokens",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccessTokenDto.class)
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
        //TODO: fix documentation
    ResponseEntity<AccessTokenDto> refresh(String refreshToken);

    @Operation(
            summary = "Logout user",
            description = "Invalidates the refresh token by revoking it server-side and removing the HTTP-only cookie",
            security = @SecurityRequirement(name = "refreshToken"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Logout successful - refresh token revoked and cookie cleared",
                            headers = @Header(
                                    name = HttpHeaders.SET_COOKIE,
                                    description = "Clears the refreshToken cookie by setting Max-Age=0",
                                    schema = @Schema(type = "string", example = "refreshToken=; Path=/api/auth; HttpOnly; SameSite=Lax; Max-Age=0")
                            )
                    )
            }
    )
    ResponseEntity<Void> logout(String refreshToken);

    @Operation(hidden = true)
    ResponseEntity<Map<String, String>> test();
}
