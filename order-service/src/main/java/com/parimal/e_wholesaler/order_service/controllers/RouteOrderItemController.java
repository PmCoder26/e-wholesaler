package com.parimal.e_wholesaler.order_service.controllers;

import com.parimal.e_wholesaler.order_service.services.RouteOrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/route-order-item")
@AllArgsConstructor
public class RouteOrderItemController {

    private final RouteOrderItemService routeOrderItemService;



}
