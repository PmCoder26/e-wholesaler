package com.parimal.e_wholesaler.order_service.controllers;

import com.parimal.e_wholesaler.order_service.dtos.DeleteRequestDTO;
import com.parimal.e_wholesaler.order_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.order_service.dtos.SalesUpdateRequestDTO;
import com.parimal.e_wholesaler.order_service.dtos.ShopOrderStatusUpdateDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderRequestDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderResponseDTO;
import com.parimal.e_wholesaler.order_service.services.ShopOrderService;
import jakarta.servlet.http.HttpServletRequest;
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
            HttpServletRequest request,
            @RequestBody @Valid ShopOrderRequestDTO requestDTO
    ) {
        return shopOrderService.createOrder(request, requestDTO);
    }

    @GetMapping(path = "/{id}")
    public ShopOrderDTO getOrderById(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        return shopOrderService.getOrderById(request, id);
    }

    @DeleteMapping
    public MessageDTO deleteOrderById(
            HttpServletRequest request,
            @RequestBody @Valid DeleteRequestDTO requestDTO
    ) {
        return shopOrderService.deleteOrderById(request, requestDTO);
    }

    @PutMapping(path = "/update-status")
    public MessageDTO updateOrderStatus(
            HttpServletRequest request,
            @RequestBody @Valid ShopOrderStatusUpdateDTO requestDTO
    ) {
        return shopOrderService.updateOrderStatus(request, requestDTO);
    }

}
