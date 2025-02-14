package com.parimal.e_wholesaler.shop_service.exceptions;

import com.parimal.e_wholesaler.shop_service.advices.ApiError;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public class MyException extends RuntimeException {

    public ApiError apiError;

}
