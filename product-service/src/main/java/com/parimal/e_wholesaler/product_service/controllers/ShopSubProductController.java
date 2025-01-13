package com.parimal.e_wholesaler.product_service.controllers;

import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductResponseDTO;
import com.parimal.e_wholesaler.product_service.services.ShopSubProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/shop-sub-product")
@AllArgsConstructor
public class ShopSubProductController {

    private final ShopSubProductService shopSubProductService;


    @PostMapping
    public ShopSubProductResponseDTO addShopSubProduct(
            @RequestBody @Valid
            ShopSubProductRequestDTO requestDTO
    ) throws Exception {
        return shopSubProductService.addShopSubProduct(requestDTO);
    }

}
