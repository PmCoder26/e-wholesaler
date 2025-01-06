package com.parimal.e_wholesaler.product_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    private String name;

    private String category;

    private String company;

    private Long shopId;

}
