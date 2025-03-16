package com.parimal.e_wholesaler.user_service.advices;


import com.parimal.e_wholesaler.user_service.exceptions.MyException;
import com.parimal.e_wholesaler.user_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.user_service.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    public ApiResponse handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleInvalidArgument(MethodArgumentNotValidException e) {
        List<String> subErrors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();
        return buildApiError("Invalid input.", subErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse handleUnsupportedInputs(HttpMessageNotReadableException e) {
        return buildApiError("Invalid input(s) or input(s) not supported", Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MyException.class)
    public ApiResponse handleMyException(MyException e) {
        return buildApiErrorResponse(e.apiError);
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
