package com.parimal.e_wholesaler.user_service.dtos;

import com.parimal.e_wholesaler.user_service.utils.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private String accessToken;

    private String refreshToken;

    private UserType userType;

    private Long userTypeId;

}
