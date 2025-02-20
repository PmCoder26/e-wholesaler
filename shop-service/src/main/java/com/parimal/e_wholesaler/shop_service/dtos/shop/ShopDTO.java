package com.parimal.e_wholesaler.shop_service.dtos.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopDTO {

    private Long id;

    private String name;

    private String gstNo;

    private String address;

    private String city;

    private String state;

    private LocalDateTime createdAt;

}
