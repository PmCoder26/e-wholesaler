package com.parimal.e_wholesaler.order_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    private Long id;

    private Long subProductId;

    private Long quantity;

    private Double price;

    private Long orderId;

}
