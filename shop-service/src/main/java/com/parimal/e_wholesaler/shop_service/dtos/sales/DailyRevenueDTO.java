package com.parimal.e_wholesaler.shop_service.dtos.sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyRevenueDTO {

    private String shopName;

    private String city;

    private Double dailyRevenue;

    private Long dailyTransactions;

}
