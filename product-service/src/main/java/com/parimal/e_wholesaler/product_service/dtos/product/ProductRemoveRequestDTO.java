package com.parimal.e_wholesaler.product_service.dtos.product;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductRemoveRequestDTO {

    @NotNull(message = "Shop id cannot be null.")
    private Long shopId;

    @NotEmpty(message = "Shop name cannot be empty.")
    @NotNull(message = "Shop name cannot be null.")
    private String productName;

}
