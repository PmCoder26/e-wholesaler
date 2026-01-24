package com.parimal.e_wholesaler.common.dtos.sub_product;

import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubProductDTO2 {

    private Long id;          // DB-generated sub-product ID
    private Double mrp;
    private List<SellingUnitDTO> sellingUnits = new ArrayList<>();

}
