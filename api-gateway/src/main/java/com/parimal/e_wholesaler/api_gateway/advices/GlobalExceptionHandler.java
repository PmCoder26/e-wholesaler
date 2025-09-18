package com.parimal.e_wholesaler.api_gateway.advices;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        return buildApiErrorResponse(e.getMessage(), Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ApiResponse handleExpiredJwtException(ExpiredJwtException e) {
        return buildApiErrorResponse("JWT Expired", Collections.emptyList(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ApiResponse handleJwtException(JwtException e) {
        return buildApiErrorResponse(e.getMessage(), Collections.emptyList(), HttpStatus.UNAUTHORIZED);
    }

    private ApiResponse buildApiErrorResponse(String message, List<String> subErrors, HttpStatus status) {
        ApiError apiError = ApiError.builder()
                .message(message)
                .subErrors(subErrors)
                .status(status)
                .build();
        return new ApiResponse(apiError);
    }

}
