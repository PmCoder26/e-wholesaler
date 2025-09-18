package com.parimal.e_wholesaler.sales_service.advices;


import com.parimal.e_wholesaler.sales_service.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse handleResourceNotFound(ResourceNotFoundException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ApiResponse handleResourceAlreadyExists(ResourceAlreadyExistsException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ApiResponse handleUnAuthorized(UnAuthorizedAccessException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MyException.class)
    public ApiResponse handleMyException(MyException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), e.apiError.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse handleRuntimeException(RuntimeException e) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ApiResponse(apiError);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ApiResponse handleForbiddenException(ForbiddenException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.NOT_FOUND);
    }

    private ApiResponse buildApiError(String message, List<String> subErrors, HttpStatus status) {
        ApiError apiError = ApiError.builder()
                .message(message)
                .subErrors(subErrors)
                .status(status)
                .build();
        return buildApiErrorResponse(apiError);
    }

    private ApiResponse buildApiErrorResponse(ApiError apiError){
        return new ApiResponse(apiError);
    }

}
