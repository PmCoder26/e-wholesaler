package com.parimal.e_wholesaler.product_service.controllers;

import com.parimal.e_wholesaler.product_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.product_service.dtos.ShopProductDTO;
import com.parimal.e_wholesaler.product_service.dtos.SubProductStockUpdateDTO;
import com.parimal.e_wholesaler.product_service.dtos.product.ProductRemoveRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.*;
import com.parimal.e_wholesaler.product_service.services.ShopSubProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(path = "/shop/{shopId}/shop-sub-products")
    public List<ShopProductDTO> getShopSubProductsByShopId(
            HttpServletRequest request,
            @PathVariable Long shopId
    ) {
        return shopSubProductService.getShopSubProductsByShopId(request, shopId);
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

    @PutMapping
    public MessageDTO updateShopSubProduct(
            HttpServletRequest request,
            @RequestBody @Valid
            ShopSubProductUpdateRequestDTO requestDTO
    ) {
        return shopSubProductService.updateShopSubProduct(request, requestDTO);
    }

    @DeleteMapping(path = "/remove-product")
    public MessageDTO removeProductByShopIdAndProductName(
            HttpServletRequest request,
            @RequestBody @Valid
            ProductRemoveRequestDTO requestDTO
    ) {
        return shopSubProductService.removeProductByShopIdAndProductName(request, requestDTO);
    }

}
