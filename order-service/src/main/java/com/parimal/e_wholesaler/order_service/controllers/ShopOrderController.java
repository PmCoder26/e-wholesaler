package com.parimal.e_wholesaler.order_service.controllers;

import com.parimal.e_wholesaler.order_service.dtos.DeleteRequestDTO;
import com.parimal.e_wholesaler.order_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderRequestDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderResponseDTO;
import com.parimal.e_wholesaler.order_service.services.ShopOrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/shop-order")
@AllArgsConstructor
public class ShopOrderController {

    private final ShopOrderService shopOrderService;


    @PostMapping
    public ShopOrderResponseDTO createOrder(
            @RequestBody @Valid ShopOrderRequestDTO requestDTO
    ) {
        return shopOrderService.createOrder(requestDTO);
    }

    @GetMapping(path = "/{id}")
    public ShopOrderDTO getOrderById(
            @PathVariable Long id
    ) {
        return shopOrderService.getOrderById(id);
    }

    @DeleteMapping
    public MessageDTO deleteOrderById(
            @RequestBody @Valid DeleteRequestDTO requestDTO
    ) {
        return shopOrderService.deleteOrderById(requestDTO);
    }

}
