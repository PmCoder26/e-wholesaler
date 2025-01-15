package com.parimal.e_wholesaler.order_service.dtos.shop;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopOrderItemRequestDTO {

    @NotNull(message = "Sub-product id cannot be null.")
    private Long subProductId;

    @NotNull(message = "Quantity cannot be null.")
    private Long quantity;

    @NotNull(message = "Price id cannot be null.")
    private Long price;

    @NotNull(message = "Order id cannot be null.")
    private Long orderId;

    @NotNull(message = "Shop id cannot be null.")
    private Long shopId;

}
