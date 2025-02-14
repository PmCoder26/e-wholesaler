package com.parimal.e_wholesaler.shop_service.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesRequestDTO {

    @NotNull(message = "Shop id cannot be null.")
    private Long shopId;

}
