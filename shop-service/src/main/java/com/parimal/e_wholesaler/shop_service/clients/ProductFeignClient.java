package com.parimal.e_wholesaler.shop_service.clients;

import com.parimal.e_wholesaler.shop_service.advices.ApiResponse;
import com.parimal.e_wholesaler.shop_service.dtos.ProductDTO;
import com.parimal.e_wholesaler.shop_service.dtos.ShopProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "product-service", path = "/products/shop-sub-product")
public interface ProductFeignClient {

    @GetMapping(path = "/shop/{shopId}/shop-sub-products")
    ApiResponse<List<ShopProductDTO>> getShopSubProductsByShopId(@PathVariable Long shopId);

}
