package com.parimal.e_wholesaler.product_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopProductDTO {

    private String name;

    private String category;

    private String company;

    List<ShopSubProduct2DTO> shopSubProducts;

}
