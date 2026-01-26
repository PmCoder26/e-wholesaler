package com.parimal.e_wholesaler.shop_service.clients;

import com.parimal.e_wholesaler.common.advices.ApiResponse;
import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopRequestDTO;
import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopResponseDTO;
import com.parimal.e_wholesaler.common.dtos.product.ProductIdentityDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "product-service", path = "/products/internal/shops", contextId = "ownerProductClient")
public interface ProductFeignClient {

    @PostMapping(path = "/{shopId}/products")
    ApiResponse<AddProductForShopResponseDTO> addProduct(@PathVariable Long shopId, AddProductForShopRequestDTO requestDTO);

    @GetMapping(path = "/{shopId}/owner/products")
    ApiResponse<List<ProductIdentityDTO>> getShopProductForOwner(@PathVariable Long shopId);

    @DeleteMapping(path = "/{shopId}/products/{productId}")
    ApiResponse<Void> deleteShopProduct(@PathVariable Long shopId, @PathVariable Long productId);

}
