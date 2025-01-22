package com.parimal.e_wholesaler.customer_service.dtos;

import com.parimal.e_wholesaler.customer_service.utils.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDTO {

    @NotNull(message = "name cannot be null.")
    @NotEmpty(message = "name cannot be empty.")
    private String name;

    @NotNull(message = "gender cannot be null.")
    private Gender gender;

    @NotNull(message = "mobile number cannot be null.")
    private String mobNo;

    @NotNull(message = "address cannot be null.")
    @NotEmpty(message = "address cannot be empty.")
    private String address;

    @NotNull(message = "city cannot be null.")
    @NotEmpty(message = "city cannot be empty.")
    private String city;

    @NotNull(message = "state cannot be null.")
    @NotEmpty(message = "state cannot be empty.")
    private String state;

}
