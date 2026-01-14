package com.parimal.e_wholesaler.worker_service.dtos;

import com.parimal.e_wholesaler.worker_service.utils.Gender;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerUpdateRequestDTO {

    @NotNull(message = "Id cannot be null.")
    private Long id;

    @NotNull(message = "Name cannot be null.")
    @NotEmpty(message = "Name cannot be empty.")
    private String name;

    @NotNull(message = "Gender cannot be null.")
    private Gender gender;

    @NotNull(message = "Mobile number cannot be null.")
    @NotEmpty(message = "Mobile number cannot be empty.")
    private String mobNo;

    @NotNull(message = "Address cannot be null.")
    @NotEmpty(message = "Address cannot be empty.")
    private String address;

    @NotNull(message = "City cannot be null.")
    @NotEmpty(message = "City cannot be empty.")
    private String city;

    @NotNull(message = "State cannot be null.")
    @NotEmpty(message = "State cannot be empty.")
    private String state;

    @NotNull(message = "Shop id cannot be null.")
    private Long shopId;

    @NotNull(message = "Salary cannot be null.")
    private Double salary;

}
