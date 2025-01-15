package com.parimal.e_wholesaler.order_service.dtos.shop;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopOrderRequestDTO {

    @NotNull(message = "Shop id cannot be null.")
    private Long shopId;

    @NotNull(message = "Worker id cannot be null.")
    private Long workerId;

}
