package com.parimal.e_wholesaler.worker_service.advices;

import lombok.Data;
import java.time.LocalDateTime;

@Data
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
