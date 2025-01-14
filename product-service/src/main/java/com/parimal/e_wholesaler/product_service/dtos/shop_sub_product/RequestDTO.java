package com.parimal.e_wholesaler.product_service.dtos.shop_sub_product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    @NotNull(message = "Shop sub-product id should not be null.")
    private Long shopSubProductId;

    @NotNull(message = "Shop id should not be null.")
    private Long shopId;

}
