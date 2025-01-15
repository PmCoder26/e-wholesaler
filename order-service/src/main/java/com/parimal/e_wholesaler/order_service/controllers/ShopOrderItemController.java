package com.parimal.e_wholesaler.order_service.controllers;

import com.parimal.e_wholesaler.order_service.services.ShopOrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/shop-order-item")
@AllArgsConstructor
public class ShopOrderItemController {

    private final ShopOrderItemService shopOrderItemService;



}
