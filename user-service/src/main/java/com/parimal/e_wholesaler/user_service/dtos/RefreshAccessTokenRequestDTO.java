package com.parimal.e_wholesaler.user_service.dtos;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshAccessTokenRequestDTO {

    @NotEmpty(message = "refresh token cannot be empty")
    private String refreshToken;

}
