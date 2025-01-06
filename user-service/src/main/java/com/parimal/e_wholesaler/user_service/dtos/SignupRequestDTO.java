package com.parimal.e_wholesaler.user_service.dtos;

import com.parimal.e_wholesaler.user_service.utils.UserType;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {

    @Pattern(regexp = "^\\d{10}$", message = "Invalid username, username should be mobile number of 10 digits.")
    private String username;

    private String password;

    private UserType userType;

}
