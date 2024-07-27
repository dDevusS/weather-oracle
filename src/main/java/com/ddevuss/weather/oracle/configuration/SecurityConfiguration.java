package com.ddevuss.weather.oracle.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                        .requestMatchers("/login",
                                "/authentication/**",
                                "/registration",
                                "/forecast",
                                "/",
                                "error",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "fragments").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(logging -> logging
                        .loginPage("/login")
                        .usernameParameter("login")
                        .defaultSuccessUrl("/forecast")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/forecast")
                        .deleteCookies("JSESSIONID")
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/forecast")
                        .maximumSessions(3)
                        .expiredUrl("/forecast")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
