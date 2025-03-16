package com.parimal.e_wholesaler.user_service.dtos;

import com.parimal.e_wholesaler.user_service.utils.Gender;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerRequestDTO {

    @NotNull(message = "Name must mot be null.")
    @NotEmpty(message = "Name must mot be empty")
    private String name;

    @NotNull(message = "Gender must mot be null.")
    private Gender gender;

    @Pattern(regexp = "^\\d{10}", message = "Invalid mobile number.")
    private String mobNo;

    @NotNull(message = "Address must mot be null.")
    @NotEmpty(message = "Address must mot be empty.")
    private String address;

    @NotNull(message = "City must mot be null.")
    @NotEmpty(message = "City must mot be empty.")
    private String city;

    @NotNull(message = "State must mot be null.")
    @NotEmpty(message = "State must mot be empty.")
    private String state;

}
