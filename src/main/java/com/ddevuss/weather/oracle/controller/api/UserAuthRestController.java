package com.ddevuss.weather.oracle.controller.api;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ddevuss.weather.oracle.controller.api.docs.UserAuthController;
import com.ddevuss.weather.oracle.dto.ApiErrorDto;
import com.ddevuss.weather.oracle.dto.JwtResponseDto;
import com.ddevuss.weather.oracle.dto.RefreshTokenDto;
import com.ddevuss.weather.oracle.dto.UserCreateDto;
import com.ddevuss.weather.oracle.dto.UserReadDto;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.service.JwtService;
import com.ddevuss.weather.oracle.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserAuthRestController implements UserAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
            );

            Instant createdAt = Instant.now();
            String accessToken = jwtService.generateAccessToken(authentication.getName(), createdAt);
            String refreshToken = jwtService.generateRefreshToken(authentication.getName(), createdAt);
            jwtService.saveRefreshToken(authentication.getName(), createdAt, refreshToken);

            return ResponseEntity.ok(new JwtResponseDto(accessToken, refreshToken));
        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(new ApiErrorDto("", "Invalid username or password", "VALIDATION_ERROR"));
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody @Validated UserCreateDto user) {
        try {
            UserReadDto createdUser = userService.save(user);
            return ResponseEntity.status(CREATED).body(createdUser);
        }
        catch (DataIntegrityViolationException exception) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception.getCause();
            String constraintName = constraintViolationException.getConstraintName();

            if ("users_login_key".equals(constraintName)) {
                return ResponseEntity.status(CONFLICT)
                        .body(new ApiErrorDto("DUPLICATED_LOGIN_ERROR",
                                "User with this login already exists",
                                "VALIDATION_ERROR")
                        );
            }
            else {
                log.error(exception.getMessage(), exception);

                return ResponseEntity.internalServerError()
                        .body(new ApiErrorDto("INTERNAL_ERROR", "Something went wrong. Please, try again later.", "INTERNAL"));
            }
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Validated @RequestBody RefreshTokenDto jsonRefreshToken) {
        DecodedJWT token;
        String refreshToken = jsonRefreshToken.refreshToken();

        try {
            token = jwtService.verifyAndDecodeToken(refreshToken);
        }
        catch (TokenExpiredException exception) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(new ApiErrorDto("",
                            "Expired refresh token",
                            "VALIDATION_ERROR"));
        }
        catch (JWTVerificationException exception) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(new ApiErrorDto("",
                            "Invalid refresh token",
                            "VALIDATION_ERROR"));
        }

        Instant createdAt = Instant.now();
        String accessToken = jwtService.generateAccessToken(token.getSubject(), createdAt);
        String newRefreshToken = jwtService.exchangeRefreshToken(token, createdAt);

        return ResponseEntity.ok(new JwtResponseDto(accessToken, newRefreshToken));
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Map.of("test", "success"));
    }
}
