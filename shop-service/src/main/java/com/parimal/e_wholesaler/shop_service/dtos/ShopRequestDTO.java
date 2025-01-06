package com.parimal.e_wholesaler.shop_service.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopRequestDTO {

    private String name;

    @NotNull(message = "GST number cannot be null.")
    private String gstNo;

    private String address;

    private String city;

    private String state;

    @NotNull(message = "Owner id cannot be null.")
    private Long ownerId;

}
