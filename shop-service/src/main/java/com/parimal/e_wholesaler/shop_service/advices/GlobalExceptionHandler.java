package com.parimal.e_wholesaler.shop_service.advices;


import com.parimal.e_wholesaler.shop_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.shop_service.exceptions.UnAuthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleResourceAlreadyExists(ResourceAlreadyExistsException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleInvalidArgument(MethodArgumentNotValidException e) {
        List<String> subErrors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();
        return buildApiError("Invalid input.", subErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleUnsupportedInputs(HttpMessageNotReadableException e) {
        return buildApiError("Invalid input(s) or input(s) not supported", Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ResponseEntity<ApiResponse> handleUnAuthorized(UnAuthorizedAccessException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiResponse> buildApiError(String message, List<String> subErrors, HttpStatus status) {
        ApiError apiError = ApiError.builder()
                .message(message)
                .subErrors(subErrors)
                .status(status)
                .build();
        return buildApiErrorResponse(apiError);
    }

    private ResponseEntity<ApiResponse> buildApiErrorResponse(ApiError apiError){
        return new ResponseEntity<>(new ApiResponse(apiError), apiError.getStatus());
    }

}
