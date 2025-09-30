package com.parimal.e_wholesaler.product_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopSubProduct2DTO {

    private Long id;

    private Double mrp;

    private Double sellingPrice;

    private Integer quantity;

    private Long stock;

}
