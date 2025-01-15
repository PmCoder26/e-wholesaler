package com.parimal.e_wholesaler.order_service.exceptions;

import com.parimal.e_wholesaler.order_service.advices.ApiError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyException extends RuntimeException {

    private ApiError error;

}
