package com.parimal.e_wholesaler.common.dtos.shop_selling_unit;

import com.parimal.e_wholesaler.common.enums.UnitType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SellingUnitRequestDTO {
    @NotNull(message = "Unit type cannot be null")
    private UnitType unitType;

    @NotNull(message = "Packets cannot be null")
    private Integer packets;

    @NotNull(message = "Selling price cannot be null")
    private Double sellingPrice;
}
