package com.parimal.e_wholesaler.user_service.exceptions;

import com.parimal.e_wholesaler.user_service.advices.ApiError;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public class MyException extends RuntimeException {

    public ApiError apiError;

}
