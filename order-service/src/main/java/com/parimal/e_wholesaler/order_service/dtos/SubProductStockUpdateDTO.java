package com.parimal.e_wholesaler.order_service.dtos;

import com.parimal.e_wholesaler.order_service.utils.StockUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubProductStockUpdateDTO {

    private Long shopId;

    private Long subProductId;

    private Long stock;

    private StockUpdate stockUpdate;

}
