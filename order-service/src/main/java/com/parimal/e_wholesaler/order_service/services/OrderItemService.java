package com.parimal.e_wholesaler.order_service.services;

import com.parimal.e_wholesaler.order_service.dtos.OrderItemResponseDTO;
import com.parimal.e_wholesaler.order_service.repositories.OrderItemRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    public final ModelMapper modelMapper;


    public OrderItemResponseDTO addOrderItem() {

    }

}
