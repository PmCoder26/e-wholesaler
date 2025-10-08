package com.parimal.e_wholesaler.shop_service.dtos.product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuantityToSellingPrice {

    @NotNull(message = "Quantity should not be null")
    private Integer quantity;

    @NotNull(message = "Selling Price should not be null")
    private Double sellingPrice;

    @NotNull(message = "Stock should not be null")
    private Long stock;

}
