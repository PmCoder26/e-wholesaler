package com.parimal.e_wholesaler.product_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubProductResponseDTO {

    private Long id;

    private Double price;

    private Long stock;

}
