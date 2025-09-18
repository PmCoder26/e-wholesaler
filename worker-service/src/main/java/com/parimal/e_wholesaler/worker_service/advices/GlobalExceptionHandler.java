package com.parimal.e_wholesaler.worker_service.advices;


import com.parimal.e_wholesaler.worker_service.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleMethodArgumentsNotValid(MethodArgumentNotValidException e) {
        List<String> subErrors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();
        return buildApiError("Invalid arguments.", subErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ApiResponse handleUnAuthorized(UnAuthorizedAccessException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ApiResponse handleForbiddenException(ForbiddenException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MatchException.class)
    public ApiResponse handleMyException(MyException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.BAD_REQUEST);
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
