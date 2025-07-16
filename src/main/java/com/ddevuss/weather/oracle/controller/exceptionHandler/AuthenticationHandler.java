package com.ddevuss.weather.oracle.controller.exceptionHandler;

import com.ddevuss.weather.oracle.dto.ApiErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class AuthenticationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ApiErrorDto>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ApiErrorDto> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String field = error instanceof FieldError fe ? fe.getField() : "Passwords fields";
                    return new ApiErrorDto(field, error.getDefaultMessage(), "VALIDATION_ERROR");
                })
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonParseError() {
        return ResponseEntity.badRequest()
                .body(new ApiErrorDto("JSON_PARSE_ERROR", "Malformed JSON or missing fields", "VALIDATION_ERROR"));
    }

    //TODO: move to another handler
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMismatchError() {
        return ResponseEntity.badRequest()
                .body(new ApiErrorDto("", "Parameter has invalid value.", "Mismatch_ERROR"));
    }

    //TODO: move to Global handler?
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> handleGlobalException() {
        return ResponseEntity.internalServerError().body(new ApiErrorDto("INTERNAL_ERROR", "Something went wrong", "INTERNAL"));
    }
}
