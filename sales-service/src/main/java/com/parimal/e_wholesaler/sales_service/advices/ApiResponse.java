package com.parimal.e_wholesaler.sales_service.advices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    private T data;
    private ApiError error;
    private String timeStamp;

    public ApiResponse(){
        this.timeStamp = LocalDateTime.now().toString();
    }
    public ApiResponse(T data){
        this();
        this.data = data;
    }
    public ApiResponse(ApiError error){
        this();
        this.error = error;
    }

}
