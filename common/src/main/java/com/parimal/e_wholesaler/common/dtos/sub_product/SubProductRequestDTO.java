package com.parimal.e_wholesaler.common.dtos.sub_product;

import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubProductRequestDTO {

    @NotNull(message = "MRP must be provided for the sub-product")
    private Double mrp;

    @NotEmpty(message = "Selling-units must be available in at least one shop")
    @NotNull(message = "Selling-units cannot be null")
    @Valid
    private List<SellingUnitRequestDTO> sellingUnits = new ArrayList<>();
}

