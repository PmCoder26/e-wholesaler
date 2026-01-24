package com.parimal.e_wholesaler.common.dtos.shop_sub_product;

import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitRequestDTO;
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
public class AddShopSubProductRequestDTO {

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "SubProduct MRP cannot be null")
    private Double mrp;

    @NotNull(message = "Selling-unit list cannot be null")
    @NotEmpty(message = "Selling-unit list cannot be empty")
    @Valid
    private List<SellingUnitRequestDTO> sellingUnits;

}

