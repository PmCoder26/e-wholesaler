package com.parimal.e_wholesaler.shop_service.exceptions;

public class UnAuthorizedAccessException extends RuntimeException {

    public UnAuthorizedAccessException(String message) {
        super(message);
    }

}
