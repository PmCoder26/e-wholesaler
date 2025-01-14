package com.parimal.e_wholesaler.product_service.dtos.sub_product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubProductRequestDTO {

    @NotNull(message = "Product-id cannot be null.")
    private Long productId;

    @NotNull(message = "mrp cannot be null.")
    private Double mrp;

}
