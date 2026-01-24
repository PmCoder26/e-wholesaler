package com.parimal.e_wholesaler.common.dtos.product;

import com.parimal.e_wholesaler.common.dtos.sub_product.SubProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String company;
    private List<SubProductResponseDTO> subProducts;
}
