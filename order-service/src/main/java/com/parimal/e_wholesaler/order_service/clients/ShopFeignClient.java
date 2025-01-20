package com.parimal.e_wholesaler.order_service.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "shop-service", path = "/shops/shop")
public interface ShopFeignClient {



}
