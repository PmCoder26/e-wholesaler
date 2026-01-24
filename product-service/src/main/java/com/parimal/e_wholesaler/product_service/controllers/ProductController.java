package com.parimal.e_wholesaler.product_service.controllers;

import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopRequestDTO;
import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopResponseDTO;
import com.parimal.e_wholesaler.common.dtos.product.ProductIdentityDTO;
import com.parimal.e_wholesaler.product_service.services.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/internal/shops")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping(path = "/{shopId}/products")
    public AddProductForShopResponseDTO addProductForShop(
            @PathVariable Long shopId,
            @RequestBody @Valid AddProductForShopRequestDTO productRequest
    ) {
        return productService.addProductForShop(shopId, productRequest);
    }

    @GetMapping(path = "/{shopId}/owner/products")
    public List<ProductIdentityDTO> getShopProductForOwner(
            @PathVariable Long shopId
    ) {
        return productService.getShopProductForOwner(shopId);
    }

}
