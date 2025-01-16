package com.parimal.e_wholesaler.order_service.dtos;

import com.parimal.e_wholesaler.order_service.utils.SalesUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesUpdateRequestDTO {

    @NotNull(message = "Shop id cannot be null.")
    private Long shopId;

    @NotNull(message = "Amount cannot be null.")
    private Double amount;

    @NotNull(message = "Update mode cannot be null.")
    private SalesUpdate updateMode;

}
