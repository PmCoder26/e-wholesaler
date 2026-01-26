package com.parimal.e_wholesaler.product_service.controllers;

import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopRequestDTO;
import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopResponseDTO;
import com.parimal.e_wholesaler.common.dtos.product.ProductIdentityDTO;
import com.parimal.e_wholesaler.product_service.services.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/internal/shops")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping(path = "/{shopId}/products")
    @PreAuthorize(value = "hasAuthority('PRODUCT_CREATE')")
    public AddProductForShopResponseDTO addProductForShop(
            @PathVariable Long shopId,
            @RequestBody @Valid AddProductForShopRequestDTO productRequest
    ) {
        return productService.addProductForShop(shopId, productRequest);
    }

    @DeleteMapping(path = "/{shopId}/products/{productId}")
    @PreAuthorize(value = "hasAuthority('SHOP_PRODUCT_DELETE')")
    public void deleteShopProduct(
            @PathVariable Long shopId,
            @PathVariable Long productId
    ) {
        productService.deleteShopProduct(shopId, productId);
    }

    @GetMapping(path = "/{shopId}/owner/products")
    @PreAuthorize(value = "hasAuthority('PRODUCT_VIEW')")
    public List<ProductIdentityDTO> getShopProductForOwner(
            @PathVariable Long shopId
    ) {
        return productService.getShopProductForOwner(shopId);
    }

}
