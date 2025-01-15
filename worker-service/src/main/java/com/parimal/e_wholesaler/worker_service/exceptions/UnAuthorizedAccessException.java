package com.parimal.e_wholesaler.worker_service.exceptions;

public class UnAuthorizedAccessException extends RuntimeException {

    public UnAuthorizedAccessException(String message) {
        super(message);
    }

}
