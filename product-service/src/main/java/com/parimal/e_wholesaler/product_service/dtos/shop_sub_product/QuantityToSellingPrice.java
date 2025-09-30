package com.parimal.e_wholesaler.product_service.dtos.shop_sub_product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuantityToSellingPrice {

    private Integer quantity;

    private Double sellingPrice;

}
