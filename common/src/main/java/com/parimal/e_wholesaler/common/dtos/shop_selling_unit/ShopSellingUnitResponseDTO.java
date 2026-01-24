package com.parimal.e_wholesaler.common.dtos.shop_selling_unit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopSellingUnitResponseDTO {
    private Long id;
    private String unitType; // PIECE, BOX, etc.
    private Integer packets;
    private Double sellingPrice;
}
