package com.parimal.e_wholesaler.shop_service.controllers;

import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.ShopDTO;
import com.parimal.e_wholesaler.shop_service.dtos.ShopRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.ShopResponseDTO;
import com.parimal.e_wholesaler.shop_service.services.ShopService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/shop")
@AllArgsConstructor
public class ShopController {

    private final ShopService shopService;


    @PostMapping
    public ShopResponseDTO createShop(
            @RequestBody
            ShopRequestDTO requestDTO
    ) {
        return shopService.createShop(requestDTO);
    }

    @GetMapping(path = "/{id}")
    public ShopDTO getShopById(
            @PathVariable
            Long id
    ) {
        return shopService.getShopById(id);
    }

    @DeleteMapping(path = "/{id}")
    public MessageDTO deleteShopById(
            @PathVariable
            Long id
    ) {
        return shopService.deleteShopById(id);
    }

}
