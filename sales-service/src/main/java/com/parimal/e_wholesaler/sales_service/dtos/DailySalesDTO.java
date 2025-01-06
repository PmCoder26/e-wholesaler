package com.parimal.e_wholesaler.sales_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailySalesDTO {

    private Long id;

    private Double amount;

    private Long shopId;

}
