package com.ddevuss.weather.oracle.controller.api;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ddevuss.weather.oracle.configuration.WeatherOracleConfiguration;
import com.ddevuss.weather.oracle.controller.api.docs.UserAuthController;
import com.ddevuss.weather.oracle.dto.AccessTokenDto;
import com.ddevuss.weather.oracle.dto.UserDto;
import com.ddevuss.weather.oracle.entity.User;
import com.ddevuss.weather.oracle.service.JwtService;
import com.ddevuss.weather.oracle.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserAuthRestController implements UserAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final WeatherOracleConfiguration configuration;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenDto> login(@Valid @RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
        );

        Instant createdAt = Instant.now();
        String accessToken = jwtService.generateAccessToken(authentication.getName(), createdAt);
        String refreshToken = jwtService.generateRefreshToken(authentication.getName(), createdAt);
        //TODO: realize 1 refresh for 1 location?
        jwtService.saveRefreshToken(authentication.getName(), createdAt, refreshToken);

        ResponseCookie cookie = createCookieForRefreshToken(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AccessTokenDto(accessToken));
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> registration(@RequestBody @Valid UserDto user) {
        UserDto createdUser = userService.save(user);
        return ResponseEntity.status(CREATED).body(createdUser);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenDto> refresh(@CookieValue(name = "refreshToken") String refreshToken) {
        DecodedJWT token = jwtService.verifyAndDecodeToken(refreshToken);

        Instant createdAt = Instant.now();
        String accessToken = jwtService.generateAccessToken(token.getSubject(), createdAt);
        String newRefreshToken = jwtService.exchangeRefreshToken(token, createdAt);

        ResponseCookie cookie = createCookieForRefreshToken(newRefreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AccessTokenDto(accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken) {

        if (refreshToken != null) {
            try {
                DecodedJWT token = jwtService.verifyAndDecodeToken(refreshToken);
                jwtService.revokeRefreshToken(token);
            }
            catch (TokenExpiredException e) {
                log.warn("Attempt to use expired refresh token to logout");
            }
            catch (JWTVerificationException e) {
                log.warn("Attempt to use invalid refresh token to logout");
            }
        }

        ResponseCookie cookie = createCookieForRefreshToken("");

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    private ResponseCookie createCookieForRefreshToken(String refreshToken) {
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(configuration.getJwt().secure())
                .path("/api/auth")
                .sameSite("Lax");

        if (refreshToken.isBlank()) {
            cookieBuilder.maxAge(0);
        }
        else {
            cookieBuilder.maxAge(configuration.getJwt().timeOfLife().refreshToken());
        }

        return cookieBuilder.build();
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        return ResponseEntity.ok(Map.of("test", "success"));
    }
}
