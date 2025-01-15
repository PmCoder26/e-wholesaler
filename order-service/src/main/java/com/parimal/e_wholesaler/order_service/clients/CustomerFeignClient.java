package com.parimal.e_wholesaler.order_service.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "customer-service", path = "/customer")
public interface CustomerFeignClient {

}
