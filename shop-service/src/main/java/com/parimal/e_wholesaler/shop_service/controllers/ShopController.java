package com.parimal.e_wholesaler.shop_service.controllers;

import com.parimal.e_wholesaler.shop_service.dtos.DataDTO;
import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopResponseDTO;
import com.parimal.e_wholesaler.shop_service.services.ShopService;
import io.jsonwebtoken.Header;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

@RestController
@RequestMapping(path = "/shop")
@AllArgsConstructor
public class ShopController {

    private final ShopService shopService;


    @PostMapping
    public ShopResponseDTO createShop(
            HttpServletRequest request,
            @RequestBody @Valid
            ShopRequestDTO requestDTO
    ) {
        return shopService.createShop(request, requestDTO);
    }

    @GetMapping(path = "/{id}")
    public ShopDTO getShopById(
            HttpServletRequest request,
            @PathVariable
            Long id
    ) {
        return shopService.getShopById(request, id);
    }

    @DeleteMapping(path = "/{id}")
    public MessageDTO deleteShopById(
            HttpServletRequest request,
            @PathVariable
            Long id
    ) {
        return shopService.deleteShopById(request, id);
    }

    @GetMapping(path = "/exists/{id}")
    public DataDTO<Boolean> shopExistsById(
            HttpServletRequest request,
            @PathVariable
            Long id
    ) {
        return shopService.existsById(request, id);
    }

}
