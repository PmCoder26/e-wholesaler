package com.parimal.e_wholesaler.shop_service.dtos.owner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerHomeDTO {

    private Integer shopCount;

    private Long creatingOrderCount;

    private Long workerCount;

    private Double salesAmount;

}
