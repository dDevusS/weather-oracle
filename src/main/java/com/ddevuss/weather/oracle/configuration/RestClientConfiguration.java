package com.ddevuss.weather.oracle.configuration;

import com.ddevuss.weather.oracle.configuration.application.OpenWeatherApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient openWeatherRestClient(OpenWeatherApiProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.url())
                .requestInterceptor((request, body, execution) -> {
                    long startTime = System.nanoTime();
                    URI safeUri = sanitizeUri(request.getURI());
                    MDC.put("safeUrl", safeUri + " method=" + request.getMethod());

                    try {
                        var response = execution.execute(request, body);

                        log.atDebug()
                                .addArgument(request.getMethod())
                                .addArgument(safeUri)
                                .addArgument(response.getStatusCode())
                                .addArgument((System.nanoTime() - startTime) / 1_000_000)
                                .addArgument(MDC.get("username"))
                                .addArgument(MDC.get("correlationId"))
                                .log("HTTP outbound success.\nmethod={} url={} status={} ms={} username={} correlationId={}");

                        return response;
                    }
                    catch (RuntimeException ex) {
                        log.atError().setCause(ex)
                                .addArgument(request.getMethod())
                                .addArgument(safeUri)
                                .addArgument ((System.nanoTime() - startTime) / 1_000_000)
                                .addArgument(MDC.get("username"))
                                .addArgument(MDC.get("correlationId"))
                                .addArgument(ex)
                                .log("HTTP outbound failed.\nmethod={} url={} ms={} username={} correlationId={} {}");

                        throw ex;
                    }
                })
                .build();
    }

    private static URI sanitizeUri(URI uri) {
        Set<String> sensitiveParams = Set.of("appid");

        var comp = UriComponentsBuilder.fromUri(uri).build();
        var qp = new LinkedMultiValueMap<>(comp.getQueryParams());
        qp.forEach((k, v) -> {
            if (sensitiveParams.contains(k)) {
                qp.put(k, List.of("***"));
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
