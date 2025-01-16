package com.parimal.e_wholesaler.product_service.controllers;

import com.parimal.e_wholesaler.product_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.product_service.dtos.SubProductStockUpdateDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.RequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductResponseDTO;
import com.parimal.e_wholesaler.product_service.services.ShopSubProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/shop-sub-product")
@AllArgsConstructor
public class ShopSubProductController {

    private final ShopSubProductService shopSubProductService;


    @PostMapping
    public ShopSubProductResponseDTO addShopSubProduct(
            @RequestBody @Valid ShopSubProductRequestDTO requestDTO
    ) throws Exception {
        return shopSubProductService.addShopSubProduct(requestDTO);
    }

    @GetMapping
    public ShopSubProductDTO getShopSubProductById(
            @RequestBody @Valid RequestDTO requestDTO
    ) throws Exception {
        return shopSubProductService.getShopSubProductById(requestDTO);
    }

    @GetMapping(path = "/{subProductId}/shop/{shopId}")
    public ShopSubProductDTO getShopSubProductByIds(
            @PathVariable Long subProductId,
            @PathVariable Long shopId
    ) {
        return shopSubProductService.getShopSubProductByIds(subProductId, shopId);
    }

    @DeleteMapping
    public MessageDTO removeShopSubProductById(
            @RequestBody @Valid RequestDTO requestDTO
    ) throws Exception {
        return shopSubProductService.removeShopSubProductById(requestDTO);
    }

    @PutMapping(path = "/update-stock")
    public MessageDTO updateStock(
            @RequestBody @Valid SubProductStockUpdateDTO updateDTO
    ) {
        return shopSubProductService.updateStock(updateDTO);
    }

}
