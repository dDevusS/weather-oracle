package com.ddevuss.weather.oracle.controller.api;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ddevuss.weather.oracle.dto.UserCreateDto;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.service.JwtService;
import com.ddevuss.weather.oracle.service.UserService;
import com.ddevuss.weather.oracle.util.JsonMapper;
import jakarta.servlet.http.HttpServletResponse;
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
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserAuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        try {
            System.out.println("Entered login handler");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
            );

            Instant createdAt = Instant.now();
            String accessToken = jwtService.generateAccessToken(authentication.getName(), createdAt);
            String refreshToken = jwtService.generateRefreshToken(authentication.getName(), createdAt);
            jwtService.saveRefreshToken(authentication.getName(), createdAt, refreshToken);

            return ResponseEntity.ok(Map.of("access_token", accessToken, "refresh_token", refreshToken));
        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody @Validated UserCreateDto user) {
        try {
            userService.save(user);
            return ResponseEntity.ok().build();
        }
        catch (DataIntegrityViolationException exception) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception.getCause();
            String constraintName = constraintViolationException.getConstraintName();

            if ("users_login_key".equals(constraintName)) {
                return ResponseEntity.status(CONFLICT)
                        .body(Map.of("error", "User with this login already exists"));
            }
            else {
                log.error(exception.getMessage(), exception);

                return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "There is unexpected error. Please try again later."));
            }
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody String jsonRefreshToken) {
        DecodedJWT token;
        String refreshToken;

        try {
            refreshToken = JsonMapper.extractValue(jsonRefreshToken, "refresh_token");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing refresh token"));
        }

        try {
            token = jwtService.verifyAndDecodeToken(refreshToken);
        }
        catch (TokenExpiredException exception) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(Map.of("error", "Token is expired"));
        }
        catch (JWTVerificationException exception) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(Map.of("error", "Invalid refresh token"));
        }

        Instant createdAt = Instant.now();
        String accessToken = jwtService.generateAccessToken(token.getSubject(), createdAt);
        String newRefreshToken = jwtService.exchangeRefreshToken(token, createdAt);

        return ResponseEntity.ok(Map.of("access_token", accessToken, "refresh_token", newRefreshToken));
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Map.of("test", "success"));
    }
}
