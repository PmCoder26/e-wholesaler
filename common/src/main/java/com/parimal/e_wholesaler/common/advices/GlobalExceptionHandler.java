package com.parimal.e_wholesaler.common.advices;

import com.parimal.e_wholesaler.common.exceptions.*;
import feign.FeignException;
import feign.RetryableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse handleResourceNotFound(ResourceNotFoundException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleInputValidation(MethodArgumentNotValidException e) {
        List<String> subErrors = e
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();
        return buildApiError("Invalid input(s)", subErrors, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ApiResponse handleResourceAlreadyExists(ResourceAlreadyExistsException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCalculationException.class)
    public ApiResponse handleInvalidCalculation(InvalidCalculationException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse handleUnsupportedInputs(HttpMessageNotReadableException e) {
        return buildApiError("Invalid input(s) or input(s) not supported", Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MyException.class)
    public ApiResponse handleMyException(MyException e) {
        return buildApiError(e.getError().getMessage(), e.getError().getSubErrors(), e.getError().getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse handleRuntimeException(RuntimeException e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        return buildApiError(e.getMessage(), Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public ApiResponse handleFeignException(FeignException e) {
        // 1. DETERMINE HTTP STATUS
        // Feign returns -1 for connection issues (timeouts, load balancer failures).
        // We map -1 to 503 (Service Unavailable).
        HttpStatus status = HttpStatus.resolve(e.status());
        if (status == null) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
        }

        // 2. EXTRACT SAFE MESSAGE
        String safeMessage = extractAndSanitizeMessage(e, status);

        // 3. BUILD RESPONSE
        ApiError apiError = ApiError.builder()
                .message(safeMessage)
                .status(status)
                .subErrors(Collections.emptyList())
                .build();

        return new ApiResponse(apiError);
    }

    private String extractAndSanitizeMessage(FeignException e, HttpStatus status) {
        // CASE A: Downstream service returned a structured JSON error (Business Logic Error)
        // e.g., 404 User Not Found, 400 Validation Error
        try {
            String body = e.contentUTF8();
            if (body != null && !body.isBlank()) {
                ObjectMapper mapper = new ObjectMapper();
                ApiResponse response = mapper.readValue(body, ApiResponse.class);
                if (response.getError() != null && response.getError().getMessage() != null) {
                    return response.getError().getMessage();
                }
            }
        } catch (Exception ignored) {
            // Parsing failed, proceed to Fallback
        }

        // CASE B: Infrastructure / Network Errors (No JSON Body)
        // We must sanitize 'e.getMessage()' because it contains internal URLs/Class names.

        // 1. Check for specific Load Balancer or Connection issues
        String rawMessage = e.getMessage();
        if (e instanceof RetryableException ||
                rawMessage.contains("Load balancer does not contain an instance") ||
                rawMessage.contains("Connection refused") ||
                rawMessage.contains("connect timed out")) {
            return "Service is currently unavailable. Please try again later.";
        }

        // 2. Check for Timeouts (Read timed out)
        if (rawMessage.contains("Read timed out")) {
            return "The request timed out waiting for a response.";
        }

        // 3. Handle standard HTTP errors based on status code if no specific message exists
        if (status == HttpStatus.NOT_FOUND) {
            return "The requested resource was not found.";
        }
        if (status == HttpStatus.FORBIDDEN || status == HttpStatus.UNAUTHORIZED) {
            return "Access is denied.";
        }

        // CASE C: Fallback for everything else (Safety Net)
        // Never return rawMessage here to avoid leaking internal details
        return "An unexpected error occurred in an external service.";
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
