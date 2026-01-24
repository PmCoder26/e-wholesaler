package com.parimal.e_wholesaler.common.dtos.sub_product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubProductResponseDTO2 {

    private String productName;
    private String company;
    private Double mrp;
    private String message;

}
