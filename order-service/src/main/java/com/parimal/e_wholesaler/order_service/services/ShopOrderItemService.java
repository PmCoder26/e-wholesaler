package com.parimal.e_wholesaler.order_service.services;

import com.parimal.e_wholesaler.order_service.advices.ApiError;
import com.parimal.e_wholesaler.order_service.advices.ApiResponse;
import com.parimal.e_wholesaler.order_service.clients.SubProductFeignClient;
import com.parimal.e_wholesaler.order_service.dtos.DataDTO;
import com.parimal.e_wholesaler.order_service.dtos.ShopSubProductDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderItemRequestDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderItemResponseDTO;
import com.parimal.e_wholesaler.order_service.exceptions.MyException;
import com.parimal.e_wholesaler.order_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.order_service.repositories.ShopOrderItemRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class ShopOrderItemService {

    private final ShopOrderItemRepository shopOrderItemRepository;
    private final SubProductFeignClient subProductFeignClient;
    private final ShopOrderService shopOrderService;
    private final ModelMapper modelMapper;

    public ShopOrderItemResponseDTO addOrderItem(ShopOrderItemRequestDTO requestDTO) {
        boolean orderExists = shopOrderService.existsById(requestDTO.getOrderId());
        if(!orderExists) {
            throw new ResourceNotFoundException("Order with id: " + requestDTO.getOrderId() + " not found.");
        }
        ApiResponse<ShopSubProductDTO> subProductResponse = subProductFeignClient.getShopSubProductByIds(
                requestDTO.getSubProductId(), requestDTO.getShopId());
        ShopSubProductDTO subProduct = subProductResponse.getData();
        if(subProduct == null) {
            throw new MyException(subProductResponse.getError());
        }
        if(subProduct.getStock() < requestDTO.getQuantity()) {
            throw new MyException()
        }
    }

}
