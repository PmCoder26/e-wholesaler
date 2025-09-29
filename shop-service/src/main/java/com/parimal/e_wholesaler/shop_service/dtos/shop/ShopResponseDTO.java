package com.parimal.e_wholesaler.shop_service.dtos.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopResponseDTO {

    private Long id;
    private String name;
    private String gstNo;
    private String address;
    private String city;
    private String state;

}
