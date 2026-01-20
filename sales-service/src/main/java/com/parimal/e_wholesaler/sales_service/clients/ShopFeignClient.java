package com.parimal.e_wholesaler.sales_service.clients;

import com.parimal.e_wholesaler.common.advices.ApiResponse;
import com.parimal.e_wholesaler.sales_service.dtos.DataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "shop-service", path = "/shops/shop")
public interface ShopFeignClient {

//    @GetMapping(path = "/{id}")
//    public ApiResponse<ShopDTO> getShopById(@PathVariable Long id);

    @GetMapping(path = "/exists/{id}")
    ApiResponse<DataDTO<Boolean>> shopExistsById(@PathVariable Long id);

}

