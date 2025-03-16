package com.parimal.e_wholesaler.user_service.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    @Pattern(regexp = "^\\d{10}$", message = "Invalid username, username should be mobile number of 10 digits.")
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;


}
