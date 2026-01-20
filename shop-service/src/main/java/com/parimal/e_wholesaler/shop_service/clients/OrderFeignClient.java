package com.parimal.e_wholesaler.shop_service.clients;

import com.parimal.e_wholesaler.common.advices.ApiResponse;
import com.parimal.e_wholesaler.shop_service.dtos.PairDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "order-service", path = "/orders/shop-order")
public interface OrderFeignClient {

    @PostMapping(path = "/count")
    ApiResponse<Long> getOrderCount(@RequestBody List<Long> shopIdList);

    @PostMapping(path = "/daily-orders-count")
    ApiResponse<List<PairDTO<Long, Long>>> getDailyOrdersCountByShopIdList(List<Long> shopIdList);

}
