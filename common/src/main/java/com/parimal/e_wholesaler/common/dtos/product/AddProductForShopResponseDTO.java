package com.parimal.e_wholesaler.common.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductForShopResponseDTO {

    private String message;

    private ProductDTO2 product; // contains new or relevant sub-products for this shop

}