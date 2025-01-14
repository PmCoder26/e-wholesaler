package com.parimal.e_wholesaler.product_service.controllers;

import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.RequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductResponseDTO;
import com.parimal.e_wholesaler.product_service.services.ShopSubProductService;
import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
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

    @DeleteMapping
    public MessageDTO removeShopSubProductById(
            @RequestBody @Valid RequestDTO requestDTO
    ) throws Exception {
        return shopSubProductService.removeShopSubProductById(requestDTO);
    }

}
