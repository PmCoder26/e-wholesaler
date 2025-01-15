package com.parimal.e_wholesaler.order_service.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRequestDTO {

    @NotNull(message = "Order id cannot be null.")
    private Long orderId;

    @NotNull(message = "Worker id cannot be null.")
    private Long workerId;

    @NotNull(message = "Shop id cannot be null.")
    private Long shopId;

}
