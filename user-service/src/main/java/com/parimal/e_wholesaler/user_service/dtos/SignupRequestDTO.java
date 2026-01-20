package com.parimal.e_wholesaler.user_service.dtos;

import com.parimal.e_wholesaler.common.enums.Gender;
import com.parimal.e_wholesaler.common.enums.UserType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {

    @Pattern(regexp = "^\\d{10}$", message = "Invalid username, username should be mobile number of 10 digits.")
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    private UserType userType;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private Gender gender;

    @NotNull
    @NotEmpty
    private String mobNo;

    @NotNull
    @NotEmpty
    private String address;

    @NotNull
    @NotEmpty
    private String city;

    @NotNull
    @NotEmpty
    private String state;

}
