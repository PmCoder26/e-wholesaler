package com.parimal.e_wholesaler.shop_service.clients;

import com.parimal.e_wholesaler.shop_service.advices.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "order-service", path = "/orders/shop-order")
public interface OrderFeignClient {

    @PostMapping(path = "/count")
    ApiResponse<Long> getOrderCount(@RequestBody List<Long> shopIdList);

}
