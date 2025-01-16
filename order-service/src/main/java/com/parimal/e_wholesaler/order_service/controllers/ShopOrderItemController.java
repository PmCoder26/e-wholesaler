package com.parimal.e_wholesaler.order_service.controllers;

import com.parimal.e_wholesaler.order_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.order_service.dtos.OrderItemDeleteRequestDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderItemDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderItemRequestDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderItemResponseDTO;
import com.parimal.e_wholesaler.order_service.services.ShopOrderItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/shop-order-item")
@AllArgsConstructor
public class ShopOrderItemController {

    private final ShopOrderItemService shopOrderItemService;


    @PostMapping
    public ShopOrderItemResponseDTO addOrderItem(
            @RequestBody @Valid ShopOrderItemRequestDTO requestDTO
    ) {
        return shopOrderItemService.addOrderItem(requestDTO);
    }

    @GetMapping(path = "/{id}")
    public ShopOrderItemDTO getOrderItemById(
            @PathVariable Long id
    ) {
        return shopOrderItemService.getOrderItemById(id);
    }

    @DeleteMapping
    public MessageDTO removeOrderItem(
            @RequestBody @Valid OrderItemDeleteRequestDTO requestDTO
    ) {
        return shopOrderItemService.removeOrderItem(requestDTO);
    }

}
