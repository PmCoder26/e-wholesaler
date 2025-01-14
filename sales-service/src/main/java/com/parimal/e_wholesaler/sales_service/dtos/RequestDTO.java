package com.parimal.e_wholesaler.sales_service.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    @NotNull(message = "Sales id cannot be null.")
    private Long salesId;

    @NotNull(message = "Shop id cannot be null.")
    private Long shopId;

}
