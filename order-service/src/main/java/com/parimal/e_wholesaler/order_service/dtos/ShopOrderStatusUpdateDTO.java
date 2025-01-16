package com.parimal.e_wholesaler.order_service.dtos;

import com.parimal.e_wholesaler.order_service.utils.OrderStatus;
import com.parimal.e_wholesaler.order_service.utils.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopOrderStatusUpdateDTO {

    @NotNull(message = "Shop id cannot be null.")
    private Long shopId;

    @NotNull(message = "Worker id cannot be null.")
    private Long workerId;

    @NotNull(message = "Order id cannot be null.")
    private Long orderId;

    @NotNull(message = "Order status cannot be null.")
    private OrderStatus status;

    private PaymentMethod paymentMethod;

}
