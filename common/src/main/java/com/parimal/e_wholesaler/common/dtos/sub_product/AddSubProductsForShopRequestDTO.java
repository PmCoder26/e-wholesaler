package com.parimal.e_wholesaler.common.dtos.sub_product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSubProductsForShopRequestDTO {

    @NotNull(message = "Sub-products cannot be is required")
    @NotEmpty(message = "At least one sub-product is required")
    @Valid
    private List<SubProductRequestDTO> subProducts;

}
