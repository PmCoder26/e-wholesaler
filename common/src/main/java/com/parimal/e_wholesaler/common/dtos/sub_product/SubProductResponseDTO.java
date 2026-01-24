package com.parimal.e_wholesaler.common.dtos.sub_product;

import com.parimal.e_wholesaler.common.dtos.shop_sub_product.ShopSubProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubProductResponseDTO {
    private Long id;
    private Double mrp;
    private List<ShopSubProductResponseDTO> shopSubProducts;
}
