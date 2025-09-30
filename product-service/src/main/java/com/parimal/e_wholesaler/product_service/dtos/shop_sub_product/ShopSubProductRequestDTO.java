package com.parimal.e_wholesaler.product_service.dtos.shop_sub_product;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopSubProductRequestDTO {

    @NotNull(message = "Product name should not be null")
    @NotEmpty(message = "Product name should not be empty")
    private String productName;

    @NotNull(message = "Product category should not be null")
    @NotEmpty(message = "Product category should not be empty")
    private String category;

    @NotNull(message = "Product company should not be null")
    @NotEmpty(message = "Product company should not be empty")
    private String company;

    @NotNull(message = "Product MRP -> Selling-Price(s) should not be null")
    @NotEmpty(message = "Product MRP -> Selling-Price(s) should not be empty")
    private HashMap<Double, QuantityToSellingPrice> mrpToSelling;

    @NotNull(message = "Shop id should not be null")
    private Long shopId;

}
