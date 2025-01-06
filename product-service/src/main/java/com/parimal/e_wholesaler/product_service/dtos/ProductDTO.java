package com.parimal.e_wholesaler.product_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;

    private String name;

    private String category;

    private String company;

    private Long shopId;

    private LocalDateTime createdAt;

}
