package com.ddevuss.weather.oracle.common.logging;

import com.ddevuss.weather.oracle.common.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.ddevuss.weather.oracle.common.Constants.CORRELATION_ID;

@Component
public class CorrelationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            MDC.put(CORRELATION_ID, getCorrelationId(request));
            filterChain.doFilter(request, response);
        }
        finally {
            MDC.clear();
        }
    }

    private  String getCorrelationId(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("X-Correlation-Id"))
                .orElse(UUID.randomUUID().toString());
    }
}
