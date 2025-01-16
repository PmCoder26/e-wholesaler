package com.parimal.e_wholesaler.order_service.clients;

import com.parimal.e_wholesaler.order_service.advices.ApiResponse;
import com.parimal.e_wholesaler.order_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.order_service.dtos.ShopSubProductDTO;
import com.parimal.e_wholesaler.order_service.dtos.SubProductStockUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service", path = "/shop-sub-product")
public interface SubProductFeignClient {

    @GetMapping(path = "/{subProductId}/shop/{shopId}")
    ApiResponse<ShopSubProductDTO> getShopSubProductByIds(
            @PathVariable Long subProductId, @PathVariable Long shopId);

    @PutMapping(path = "/update-stock")
    ApiResponse<MessageDTO> updateStock(@RequestBody SubProductStockUpdateDTO updateDTO);

}
