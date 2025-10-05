package com.ddevuss.weather.oracle.common.client.logging;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

import static com.ddevuss.weather.oracle.common.Constants.CORRELATION_ID;
import static com.ddevuss.weather.oracle.common.Constants.HIDDEN_VALUE;
import static com.ddevuss.weather.oracle.common.Constants.PARAM_APPID;
import static com.ddevuss.weather.oracle.common.Constants.SAFE_URL;
import static com.ddevuss.weather.oracle.common.Constants.USERNAME;

@Slf4j
@Configuration
public class ClientLoggingInterceptorConfig {

    private static final String HTTP_OUTBOUND_SUCCESS_MESSAGE_TEMPLATE = "HTTP outbound success.\nmethod={} url={} status={} ms={} username={} correlationId={}";
    private static final String HTTP_OUTBOUND_FAILED_MESSAGE_TEMPLATE = "HTTP outbound failed.\nmethod={} url={} ms={} username={} correlationId={} {}";

    @Bean
    public ClientHttpRequestInterceptor openWeatherLoggingInterceptor() {
        return (request, body, execution) -> {
            long startTime = System.nanoTime();
            URI safeUri = sanitizeUri(request.getURI());
            MDC.put(SAFE_URL, safeUri + " method=" + request.getMethod());

            try {
                var response = execution.execute(request, body);

                log.atDebug()
                        .addArgument(request.getMethod())
                        .addArgument(safeUri)
                        .addArgument(response.getStatusCode())
                        .addArgument((System.nanoTime() - startTime) / 1_000_000)
                        .addArgument(MDC.get(USERNAME))
                        .addArgument(MDC.get(CORRELATION_ID))
                        .log(HTTP_OUTBOUND_SUCCESS_MESSAGE_TEMPLATE);

                return response;
            }
            catch (RuntimeException ex) {
                log.atError().setCause(ex)
                        .addArgument(request.getMethod())
                        .addArgument(safeUri)
                        .addArgument((System.nanoTime() - startTime) / 1_000_000)
                        .addArgument(MDC.get(USERNAME))
                        .addArgument(MDC.get(CORRELATION_ID))
                        .addArgument(ex)
                        .log(HTTP_OUTBOUND_FAILED_MESSAGE_TEMPLATE);

                throw ex;
            }
        };
    }

    private static URI sanitizeUri(URI uri) {
        Set<String> sensitiveParams = Set.of(PARAM_APPID);

        var comp = UriComponentsBuilder.fromUri(uri).build();
        var qp = new LinkedMultiValueMap<>(comp.getQueryParams());
        qp.forEach((k, v) -> {
            if (sensitiveParams.contains(k)) {
                qp.put(k, List.of(HIDDEN_VALUE));
            }
        });

        return UriComponentsBuilder.newInstance()
                .scheme(uri.getScheme())
                .host(uri.getHost())
                .port(uri.getPort())
                .path(uri.getPath())
                .queryParams(qp)
                .build(true)
                .toUri();
    }
}
