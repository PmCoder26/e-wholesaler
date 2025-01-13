package com.parimal.e_wholesaler.product_service.dtos.product;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    @NotNull(message = "Name should not be null.")
    @NotEmpty(message = "Name should not be empty.")
    private String name;

    @NotNull(message = "Category should not be null.")
    @NotEmpty(message = "Category should not be empty.")
    private String category;

    @NotNull(message = "Company should not be null.")
    @NotEmpty(message = "Company should not be empty.")
    private String company;

}
