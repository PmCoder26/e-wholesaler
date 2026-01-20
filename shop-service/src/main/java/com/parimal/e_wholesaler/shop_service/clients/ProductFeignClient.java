package com.parimal.e_wholesaler.shop_service.clients;

import com.parimal.e_wholesaler.common.advices.ApiResponse;
import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.product.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service", path = "/products/shop-sub-product")
public interface ProductFeignClient {

    @GetMapping(path = "/shop/{shopId}/shop-sub-products")
    ApiResponse<List<ShopProductDTO>> getShopSubProductsByShopId(@PathVariable Long shopId);

    @PostMapping
    ApiResponse<ShopSubProductResponseDTO> addShopSubProduct(@RequestBody ShopSubProductRequestDTO requestDTO);

    @DeleteMapping
    ApiResponse<MessageDTO> removeShopSubProduct(@RequestBody RequestDTO requestDTO);

    @PutMapping
    ApiResponse<MessageDTO> updateShopSubProduct(@RequestBody ShopSubProductUpdateRequestDTO requestDTO);

    @DeleteMapping(path = "/remove-product")
    ApiResponse<MessageDTO> removeProductByShopIdAndProductName(@RequestBody ProductRemoveRequestDTO requestDTO);

}
