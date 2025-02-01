package com.parimal.e_wholesaler.shop_service.exceptions;

import com.parimal.e_wholesaler.shop_service.advices.ApiError;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MyException extends RuntimeException {

    public final ApiError apiError;

}
