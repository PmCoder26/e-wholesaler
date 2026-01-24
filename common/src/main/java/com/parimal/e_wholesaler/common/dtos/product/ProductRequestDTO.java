package com.parimal.e_wholesaler.common.dtos.product;

import com.parimal.e_wholesaler.common.dtos.sub_product.SubProductRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotNull(message = "Product name cannot be null")
    private String name;

    @NotNull(message = "Company cannot be null")
    private String company;

    @NotEmpty(message = "Product must have at least one sub-product")
    @NotNull(message = "Sub-product list cannot be null")
    @Valid
    private List<SubProductRequestDTO> subProducts;

}
