package com.parimal.e_wholesaler.shop_service.dtos.shop;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopRequestDTO {

    @NotNull(message = "Name cannot be null.")
    @NotEmpty(message = "Name cannot be empty.")
    private String name;

    @NotNull(message = "GST number cannot be null.")
    @NotEmpty(message = "GST number cannot be empty.")
    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}[Z]{1}[0-9A-Z]{1}$", message = "Invalid GST Number.")
    private String gstNo;

    @NotNull(message = "Address cannot be null.")
    @NotEmpty(message = "Address cannot be empty.")
    private String address;

    @NotNull(message = "City cannot be null.")
    @NotEmpty(message = "City cannot be empty.")
    private String city;

    @NotNull(message = "State cannot be null.")
    @NotEmpty(message = "State cannot be empty.")
    private String state;

    @NotNull(message = "Owner-id cannot be null.")
    private Long ownerId;

}
