package com.parimal.e_wholesaler.product_service.dtos.shop_sub_product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopSubProductDTO {

    private Long id;

    private Double mrp;

    private Double sellingPrice;

    private Long stock;

}
