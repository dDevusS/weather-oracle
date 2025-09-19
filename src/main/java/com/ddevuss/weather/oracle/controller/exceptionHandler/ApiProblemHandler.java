package com.ddevuss.weather.oracle.controller.exceptionHandler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ddevuss.weather.oracle.utils.ProblemDetailBuilder;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class ApiProblemHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest webRequest) {

        var req = ((ServletWebRequest) webRequest).getRequest();
        var pd = ProblemDetailBuilder.forStatus(BAD_REQUEST)
                .title("Validation failed")
                .uri(req)
                .build();

        var errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return Map.of(
                                "field", fieldError.getField(),
                                "message", fieldError.getDefaultMessage());
                    }
                    else {
                        return Map.of(
                                "fields", "rawPassword and confirmRawPassword",
                                "message", error.getDefaultMessage());
                    }
                })
                .toList();

        pd.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(pd);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        var req = ((ServletWebRequest) request).getRequest();
        var pd = ProblemDetailBuilder.forStatus(BAD_REQUEST)
                .title("Malformed JSON or missing fields")
                .uri(req)
                .build();

        return ResponseEntity.badRequest().body(pd);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        var req = ((ServletWebRequest) request).getRequest();
        var pd = ProblemDetailBuilder.forStatus(BAD_REQUEST)
                .title("Parameter has invalid value")
                .uri(req)
                .build();

        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest req) {
        return ProblemDetailBuilder.forStatus(NOT_FOUND)
                .title("Entity not found")
                .uri(req)
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest req) {
        return ProblemDetailBuilder.forStatus(CONFLICT)
                .title("Data integrity violation")
                .uri(req)
                .detail(ex.getMessage())
                .build();
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ProblemDetail handleJWTVerificationException(JWTVerificationException ex, HttpServletRequest req) {
        return ProblemDetailBuilder.forStatus(UNAUTHORIZED)
                .title("Unauthorized")
                .detail(ex.getMessage())
                .uri(req)
                .build();
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ProblemDetail handleTokenExpiredException(TokenExpiredException ex, HttpServletRequest req) {
        return ProblemDetailBuilder.forStatus(UNAUTHORIZED)
                .title("Unauthorized")
                .detail(ex.getMessage())
                .uri(req)
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex,
                                                   HttpServletRequest req) {
        var pd = ProblemDetailBuilder.forStatus(BAD_REQUEST)
                .title("Validation failed")
                .uri(req)
                .build();

        pd.setProperty("errors", ex.getConstraintViolations().stream()
                .map(v -> Map.of(
                        "message", v.getMessage()))
                .toList());
        return pd;
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ProblemDetail handleRestClientResponseException(RestClientResponseException responseException, HttpServletRequest req) {
        return ProblemDetailBuilder.forStatus(INTERNAL_SERVER_ERROR)
                .title("Something went wrong")
                .uri(req)
                .build();
    }

    @ExceptionHandler(RestClientException.class)
    public ProblemDetail handleRestClientException(RestClientException clientException, HttpServletRequest req) {
        return ProblemDetailBuilder.forStatus(INTERNAL_SERVER_ERROR)
                .title("Something went wrong")
                .uri(req)
                .build();
    }

}
