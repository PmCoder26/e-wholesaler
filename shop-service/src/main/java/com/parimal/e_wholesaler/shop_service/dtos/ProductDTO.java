package com.parimal.e_wholesaler.shop_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;

    private String name;

    private String category;

    private String company;

    private Double mrp;

    private Double sellingPrice;

    private Long stock;

}
