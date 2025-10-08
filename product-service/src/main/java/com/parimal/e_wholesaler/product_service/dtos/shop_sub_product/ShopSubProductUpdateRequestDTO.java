package com.parimal.e_wholesaler.product_service.dtos.shop_sub_product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShopSubProductUpdateRequestDTO {

    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotNull(message = "Mrp cannot be null")
    private Double mrp;

    @NotNull(message = "Selling-price cannot be null")
    private Double sellingPrice;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    @NotNull(message = "Stock cannot be null")
    private Long stock;

    @NotNull(message = "Shop-id cannot be null")
    private Long shopId;

}
