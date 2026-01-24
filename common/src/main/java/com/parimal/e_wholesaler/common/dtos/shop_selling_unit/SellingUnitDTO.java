package com.parimal.e_wholesaler.common.dtos.shop_selling_unit;

import com.parimal.e_wholesaler.common.enums.UnitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellingUnitDTO {

    private Long id;          // DB-generated selling unit ID
    private UnitType unitType;
    private Integer packets;
    private Double sellingPrice;

}
