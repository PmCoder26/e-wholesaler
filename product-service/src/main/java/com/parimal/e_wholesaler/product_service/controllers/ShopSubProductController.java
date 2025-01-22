package com.parimal.e_wholesaler.product_service.controllers;

import com.parimal.e_wholesaler.product_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.product_service.dtos.SubProductStockUpdateDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.RequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductResponseDTO;
import com.parimal.e_wholesaler.product_service.services.ShopSubProductService;
import jakarta.servlet.http.HttpServletRequest;
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
            HttpServletRequest request,
            @RequestBody @Valid ShopSubProductRequestDTO requestDTO
    ) throws Exception {
        return shopSubProductService.addShopSubProduct(request, requestDTO);
    }

    @GetMapping
    public ShopSubProductDTO getShopSubProductById(
            HttpServletRequest request,
            @RequestBody @Valid RequestDTO requestDTO
    ) throws Exception {
        return shopSubProductService.getShopSubProductById(request, requestDTO);
    }

    @GetMapping(path = "/{subProductId}/shop/{shopId}")
    public ShopSubProductDTO getShopSubProductByIds(
            HttpServletRequest request,
            @PathVariable Long subProductId,
            @PathVariable Long shopId
    ) {
        return shopSubProductService.getShopSubProductByIds(request, subProductId, shopId);
    }

    @DeleteMapping
    public MessageDTO removeShopSubProductById(
            HttpServletRequest request,
            @RequestBody @Valid RequestDTO requestDTO
    ) throws Exception {
        return shopSubProductService.removeShopSubProductById(request, requestDTO);
    }

    @PutMapping(path = "/update-stock")
    public MessageDTO updateStock(
            HttpServletRequest request,
            @RequestBody @Valid SubProductStockUpdateDTO updateDTO
    ) {
        return shopSubProductService.updateStock(request, updateDTO);
    }

}
