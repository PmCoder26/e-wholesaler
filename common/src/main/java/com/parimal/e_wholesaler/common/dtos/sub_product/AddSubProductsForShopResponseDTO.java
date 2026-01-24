package com.parimal.e_wholesaler.common.dtos.sub_product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSubProductsForShopResponseDTO {

    private String message;
    private List<SubProductDTO2> addedSubProducts;

}
