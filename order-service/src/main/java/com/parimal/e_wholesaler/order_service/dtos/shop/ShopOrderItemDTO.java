package com.parimal.e_wholesaler.order_service.dtos.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopOrderItemDTO {

    private Long id;

    private Long subProductId;

    private Long quantity;

    private Double amount;

}
