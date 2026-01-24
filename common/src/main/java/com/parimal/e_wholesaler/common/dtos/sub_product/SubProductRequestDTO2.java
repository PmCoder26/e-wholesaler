package com.parimal.e_wholesaler.common.dtos.sub_product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubProductRequestDTO2 {

    @NotNull(message = "MRP is required")
    @Positive(message = "MRP must be positive")
    private Double mrp;

}
