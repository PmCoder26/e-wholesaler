package com.parimal.e_wholesaler.product_service.advices;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.parimal.e_wholesaler.product_service.exceptions.ForbiddenException;
import com.parimal.e_wholesaler.product_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.product_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.product_service.exceptions.UnAuthorizedAccessException;
import feign.FeignException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
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
    public ApiResponse handleException(MethodArgumentNotValidException e) {
        List<String> subErrors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return buildApiError("Invalid arguments.", subErrors, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ApiResponse handleUnAuthorized(UnAuthorizedAccessException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ApiResponse handleForbiddenException(ForbiddenException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return buildApiError("Duplicate data or phone number already exists.", Collections.emptyList(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public ApiResponse handleFeignException(FeignException e) {
        HttpStatus status = HttpStatus.valueOf(e.status());

        ApiError apiError = ApiError.builder()
                .message(extractMessage(e))
                .status(status)
                .subErrors(Collections.emptyList())
                .build();

        return new ApiResponse(apiError);
    }

    private String extractMessage(FeignException e) {
        try {
            String body = e.contentUTF8();

            ObjectMapper mapper = new ObjectMapper();
            ApiResponse response = mapper.readValue(body, ApiResponse.class);

            if (response.getError() != null) {
                return response.getError().getMessage();
            }
        } catch (Exception ignored) {}

        return e.getMessage(); // fallback
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
