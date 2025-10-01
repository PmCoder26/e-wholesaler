package com.parimal.e_wholesaler.shop_service.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopSubProductResponseDTO {

    private Long productId;

    private HashMap<Long, Double> idToPriceMap;

}
