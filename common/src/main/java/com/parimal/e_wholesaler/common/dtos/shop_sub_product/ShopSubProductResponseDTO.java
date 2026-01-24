package com.parimal.e_wholesaler.common.dtos.shop_sub_product;

import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.ShopSellingUnitResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopSubProductResponseDTO {
    private Long id;
    private Long shopId;
    private List<ShopSellingUnitResponseDTO> sellingUnits;
}
