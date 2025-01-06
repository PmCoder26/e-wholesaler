package com.parimal.e_wholesaler.customer_service.dtos;

import com.parimal.e_wholesaler.customer_service.utils.Gender;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDTO {

    private String name;

    private Gender gender;

    @NotNull(message = "Mobile number should not be null")
    @Min(value = 10, message = "Invalid mobile number")
    @Max(value = 10, message = "Invalid mobile number")
    private Long mobNo;

    private String address;

    private String city;

    private String state;

}
