package com.parimal.e_wholesaler.sales_service.dtos;

import com.parimal.e_wholesaler.sales_service.utils.SalesUpdateMode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesUpdateRequestDTO {

    @NotNull(message = "Sales id cannot be null.")
    private Long salesId;

    @NotNull(message = "Amount cannot be null.")
    private Double amount;

    @NotNull(message = "Update mode cannot be null.")
    private SalesUpdateMode updateMode;

}
