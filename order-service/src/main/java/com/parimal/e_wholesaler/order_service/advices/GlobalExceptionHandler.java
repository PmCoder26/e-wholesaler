package com.parimal.e_wholesaler.order_service.advices;


import com.parimal.e_wholesaler.order_service.exceptions.InvalidCalculationException;
import com.parimal.e_wholesaler.order_service.exceptions.MyException;
import com.parimal.e_wholesaler.order_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.order_service.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<String> subErrors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();
        return buildApiError("Invalid arguments", subErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCalculationException.class)
    public ResponseEntity<ApiResponse> handleInvalidCalculation(InvalidCalculationException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<ApiResponse> handleException(MyException e) {
        return buildApiError(e.getError().getMessage(), e.getError().getSubErrors(), e.getError().getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.BAD_REQUEST);
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
