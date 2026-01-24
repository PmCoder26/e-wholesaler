package com.parimal.e_wholesaler.common.dtos.product;

import com.parimal.e_wholesaler.common.dtos.sub_product.SubProductDTO2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO2 {
    private Long id;          // DB-generated product ID
    private String name;
    private String company;
    private List<SubProductDTO2> subProducts = new ArrayList<>();
}
