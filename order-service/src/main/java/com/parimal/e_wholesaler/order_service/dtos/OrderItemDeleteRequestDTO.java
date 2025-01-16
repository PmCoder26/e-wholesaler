package com.parimal.e_wholesaler.order_service.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDeleteRequestDTO {

    @NotNull(message = "Order-item id cannot be null.")
    private Long orderItemId;

    @NotNull(message = "Worker id cannot be null.")
    private Long workerId;

    @NotNull(message = "Shop id cannot be null.")
    private Long shopId;

}
