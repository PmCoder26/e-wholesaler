package com.parimal.e_wholesaler.common.dtos.sub_product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSubProductRequestDTO2 {

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Company is required")
    private String company;

    @NotEmpty(message = "At least one sub product is required")
    @NotNull(message = "Sub-products cannot be null")
    @Valid
    private List<SubProductRequestDTO> subProducts;
}

