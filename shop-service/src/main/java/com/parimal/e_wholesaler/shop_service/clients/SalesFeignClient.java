package com.parimal.e_wholesaler.shop_service.clients;

import com.parimal.e_wholesaler.shop_service.advices.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "sales-service", path = "/sales/sale")
public interface SalesFeignClient {

    @PostMapping(path = "/sales-amount")
    ApiResponse<Double> getSalesAmount(List<Long> shopIdList);

}
