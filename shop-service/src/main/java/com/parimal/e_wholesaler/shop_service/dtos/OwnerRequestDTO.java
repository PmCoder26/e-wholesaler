package com.parimal.e_wholesaler.shop_service.dtos;

import com.parimal.e_wholesaler.shop_service.utils.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerRequestDTO {

    private Long id;

    private String name;

    private Gender gender;

    @Min(value = 10, message = "Invalid mobile number.")
    @Max(value = 10, message = "Invalid mobile number.")
    private Long mobNo;

    private String address;

    private String city;

    private String state;

}
