package com.parimal.e_wholesaler.product_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubProductDTO {

    private Long id;

    private Double price;

    private Long stock;

    private Long productId;

    private LocalDateTime createdAt;

}

