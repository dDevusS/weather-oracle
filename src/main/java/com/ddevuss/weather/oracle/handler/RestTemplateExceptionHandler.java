package com.ddevuss.weather.oracle.handler;

import com.ddevuss.weather.oracle.exception.ApiServerErrorException;
import com.ddevuss.weather.oracle.exception.BadRequestApiServerException;
import com.ddevuss.weather.oracle.exception.QuotaFinishException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestTemplateExceptionHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        var statusCode = response.getStatusCode();

        if (statusCode == HttpStatus.BAD_REQUEST
            || statusCode == HttpStatus.NOT_FOUND) {
            throw new BadRequestApiServerException();
        }
        else if (statusCode == HttpStatus.UNAUTHORIZED) {
            throw new BadRequestApiServerException();
        }
        else if (statusCode == HttpStatus.TOO_MANY_REQUESTS) {
            throw new QuotaFinishException();
        }
        else if (statusCode.is5xxServerError()) {
            throw new ApiServerErrorException();
        }
    }

}
