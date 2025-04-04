package com.parimal.e_wholesaler.product_service.clients;

import com.parimal.e_wholesaler.product_service.advices.ApiResponse;
import com.parimal.e_wholesaler.product_service.dtos.DataDTO;
import com.parimal.e_wholesaler.product_service.dtos.ShopDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "shop-service", path = "/shops/shop")
public interface ShopFeignClient {

    @GetMapping(path = "/{id}")
    ApiResponse<ShopDTO> getShopById(@PathVariable Long id);

    @GetMapping(path = "/exists/{id}")
    ApiResponse<DataDTO<Boolean>> shopExistsById(@PathVariable(value = "id") Long id);

}
