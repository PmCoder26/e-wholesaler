package com.parimal.e_wholesaler.shop_service.controllers.shop;

import com.parimal.e_wholesaler.shop_service.dtos.DataDTO;
import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopDTO;
import com.parimal.e_wholesaler.shop_service.services.shop.ShopService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/shop")
@AllArgsConstructor
public class ShopController {

    private final ShopService shopService;

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

    @GetMapping(path = "/exists/{id}")
    public DataDTO<Boolean> shopExistsById(
            @PathVariable
            Long id
    ) {
        return shopService.existsById(id);
    }

}
