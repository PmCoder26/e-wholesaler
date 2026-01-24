package com.parimal.e_wholesaler.common.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductIdentityDTO {

    private Long productId;
    private String productName;
    private String companyName;

}