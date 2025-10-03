package com.ddevuss.weather.oracle.auth.web.internal;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ddevuss.weather.oracle.auth.domain.User;
import com.ddevuss.weather.oracle.auth.domain.UserService;
import com.ddevuss.weather.oracle.auth.dto.AccessTokenDto;
import com.ddevuss.weather.oracle.auth.dto.UserCreateDto;
import com.ddevuss.weather.oracle.auth.web.doc.AuthRestController;
import com.ddevuss.weather.oracle.security.jwt.domain.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
class AuthRestControllerImp implements AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final CookieCreatorService cookieService;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenDto> login(@RequestBody @Valid User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
        );

        Instant createdAt = Instant.now();
        String accessToken = jwtService.generateAccessToken(authentication.getName(), createdAt);
        String refreshToken = jwtService.generateAndSaveRefreshToken(authentication.getName(), createdAt);

        ResponseCookie cookie = cookieService.createRefreshTokenCookie(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AccessTokenDto(accessToken));
    }

    @PostMapping("/registration")
    public ResponseEntity<UserCreateDto> registration(@RequestBody @Valid UserCreateDto user) {
        UserCreateDto createdUser = userService.save(user);
        return ResponseEntity.status(CREATED).body(createdUser);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenDto> refresh(@CookieValue(name = "refreshToken") String refreshToken) {
        DecodedJWT token = jwtService.verifyAndDecodeToken(refreshToken);

        Instant createdAt = Instant.now();
        String accessToken = jwtService.generateAccessToken(token.getSubject(), createdAt);
        String newRefreshToken = jwtService.exchangeRefreshToken(token, createdAt);

        ResponseCookie cookie = cookieService.createRefreshTokenCookie(newRefreshToken);

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
                log.atWarn()
                        .addArgument(Optional.ofNullable(MDC.get("username")).orElse("unknown"))
                        .addArgument(MDC.get("correlationId"))
                        .log("Attempt to use expired refresh token to logout. username={} correlationId={}");
            }
            catch (JWTVerificationException e) {
                log.atWarn()
                        .addArgument(Optional.ofNullable(MDC.get("username")).orElse("unknown"))
                        .addArgument(MDC.get("correlationId"))
                        .log("Attempt to use invalid refresh token to logout. username={} correlationId={}");
            }
        }

        ResponseCookie cookie = cookieService.deleteRefreshTokenCookie();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        return ResponseEntity.ok(Map.of("test", "success"));
    }
}
