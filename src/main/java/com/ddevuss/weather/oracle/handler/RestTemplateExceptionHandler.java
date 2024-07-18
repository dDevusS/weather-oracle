package com.ddevuss.weather.oracle.handler;

import com.ddevuss.weather.oracle.exception.ApiServerErrorException;
import com.ddevuss.weather.oracle.exception.BadRequestApiServerException;
import com.ddevuss.weather.oracle.exception.QuotaFinishException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
@Component
public class RestTemplateExceptionHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        var statusCode = response.getStatusCode();

        if (statusCode == HttpStatus.BAD_REQUEST) {
            log.error("400 Bad request exception to API server");
            throw new BadRequestApiServerException();
        }
        else if (statusCode == HttpStatus.NOT_FOUND) {
            log.error("404 Not found exception from API server");
            throw new BadRequestApiServerException();
        }
        else if (statusCode == HttpStatus.UNAUTHORIZED) {
            log.error("401 Unauthorized exception from API server");
            throw new BadRequestApiServerException();
        }
        else if (statusCode == HttpStatus.TOO_MANY_REQUESTS) {
            log.warn("429 Too many requests exception to API server");
            throw new QuotaFinishException();
        }
        else if (statusCode.is5xxServerError()) {
            log.error("5xx Server error exception from API server");
            throw new ApiServerErrorException();
        }
    }

}
