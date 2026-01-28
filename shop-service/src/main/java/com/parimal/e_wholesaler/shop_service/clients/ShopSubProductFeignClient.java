package com.parimal.e_wholesaler.shop_service.clients;

import com.parimal.e_wholesaler.common.advices.ApiResponse;
import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitDTO;
import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitRequestDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.AddSubProductsForShopRequestDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.AddSubProductsForShopResponseDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.SubProductDTO2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "product-service", path = "/products/internal/shops")
public interface ShopSubProductFeignClient {

    @PostMapping(path = "/{shopId}/products/{productId}/sub-products")
    ApiResponse<AddSubProductsForShopResponseDTO> addShopSubProduct(
            @PathVariable Long shopId, @PathVariable Long productId, AddSubProductsForShopRequestDTO requestDTO);

    @GetMapping(path = "/{shopId}/products/{productId}/sub-products")
    ApiResponse<List<SubProductDTO2>> getShopProductDetails(@PathVariable Long shopId, @PathVariable Long productId);

    @DeleteMapping(path = "/{shopId}/sub-products/{shopSubProductId}")
    ApiResponse<Void> deleteShopSubProduct(@PathVariable Long shopId, @PathVariable Long shopSubProductId);

    @PutMapping(path = "/{shopId}/sub-products/{shopSubProductId}/selling-units/{sellingUnitId}")
    ApiResponse<SellingUnitDTO> updateProductSellingUnit(
            @PathVariable Long shopId, @PathVariable Long shopSubProductId, @PathVariable Long sellingUnitId,
            @RequestBody SellingUnitRequestDTO requestDTO);

    @DeleteMapping(path = "/{shopId}/sub-products/{shopSubProductId}/selling-units/{sellingUnitId}")
    ApiResponse<Void> deleteProductSellingUnit(
            @PathVariable Long shopId, @PathVariable Long shopSubProductId, @PathVariable Long sellingUnitId);
}
