package com.parimal.e_wholesaler.common.dtos.product;

import com.parimal.e_wholesaler.common.dtos.sub_product.SubProductRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductForShopRequestDTO {

    @NotNull(message = "Product name cannot be null")
    private String productName;

    @NotNull(message = "Company cannot be null")
    private String company;

    @Valid
    @NotEmpty(message = "Sub-products cannot be empty")
    @NotNull(message = "Sub-products cannot be null")
    private List<SubProductRequestDTO> subProducts = new ArrayList<>();

}