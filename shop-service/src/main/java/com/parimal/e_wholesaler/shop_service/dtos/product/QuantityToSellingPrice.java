package com.parimal.e_wholesaler.shop_service.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuantityToSellingPrice {

    private Integer quantity;

    private Double sellingPrice;

}
