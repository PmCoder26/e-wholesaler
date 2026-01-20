package com.parimal.e_wholesaler.common.exceptions;

import com.parimal.e_wholesaler.common.advices.ApiError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyException extends RuntimeException {

    private ApiError error;

}
