package com.parimal.e_wholesaler.product_service.controllers;

import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.AddSubProductsForShopRequestDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.AddSubProductsForShopResponseDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.SubProductDTO2;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductDTO;
import com.parimal.e_wholesaler.product_service.services.ShopSubProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/internal/shops")
@AllArgsConstructor
public class ShopSubProductController {

    private final ShopSubProductService shopSubProductService;


    @PostMapping(path = "/{shopId}/products/{productId}/sub-products")
    @PreAuthorize(value = "hasAuthority('PRODUCT_CREATE')")
    public AddSubProductsForShopResponseDTO addShopSubProduct(
            @PathVariable Long shopId,
            @PathVariable Long productId,
            @RequestBody @Valid AddSubProductsForShopRequestDTO requestDTO
    )  {
        return shopSubProductService.addSubProductsForShop(shopId, productId, requestDTO);
    }

    @GetMapping(path = "/{shopId}/products/{productId}/sub-products")
    @PreAuthorize(value = "hasAuthority('PRODUCT_VIEW')")
   public List<SubProductDTO2> getShopProductDetails(
           @PathVariable Long shopId,
           @PathVariable Long productId
   ) {
        return shopSubProductService.getShopProductDetails(shopId, productId);
   }

    @GetMapping(path = "/{subProductId}/shop/{shopId}")
    @PreAuthorize(value = "hasAuthority('PRODUCT_VIEW')")
    public ShopSubProductDTO getShopSubProductByIds(
            @PathVariable Long subProductId,
            @PathVariable Long shopId
    ) {
        return shopSubProductService.getShopSubProductByIds(subProductId, shopId);
    }

    @DeleteMapping(path = "/{shopId}/sub-products/{shopSubProductId}")
    @PreAuthorize(value = "hasAuthority('SHOP_PRODUCT_DELETE')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteShopSubProduct(
            @PathVariable Long shopId,
            @PathVariable Long shopSubProductId
    ) {
        shopSubProductService.deleteShopSubProduct(shopId, shopSubProductId);
    }

    @PutMapping(path = "/{shopId}/sub-products/{shopSubProductId}/selling-units/{sellingUnitId}")
    @PreAuthorize(value = "hasAuthority('SHOP_PRODUCT_UPDATE')")
    public SellingUnitDTO updateProductSellingUnit(
            @PathVariable Long shopId,
            @PathVariable Long shopSubProductId,
            @PathVariable Long sellingUnitId,
            @RequestBody Map<String, Object> updates
    ) {
        return shopSubProductService.updateProductSellingUnit(shopId, shopSubProductId, sellingUnitId, updates);
    }

    @DeleteMapping(path = "/{shopId}/sub-products/{shopSubProductId}/selling-units/{sellingUnitId}")
    @PreAuthorize(value = "hasAuthority('SHOP_PRODUCT_DELETE')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductSellingUnit(
            @PathVariable Long shopId,
            @PathVariable Long shopSubProductId,
            @PathVariable Long sellingUnitId
    ) {
        shopSubProductService.deleteProductSellingUnit(shopId, shopSubProductId, sellingUnitId);
    }

}
