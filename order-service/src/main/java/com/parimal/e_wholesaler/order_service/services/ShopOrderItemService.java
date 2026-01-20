package com.parimal.e_wholesaler.order_service.services;

import com.parimal.e_wholesaler.common.advices.ApiResponse;
import com.parimal.e_wholesaler.order_service.clients.SalesFeignClient;
import com.parimal.e_wholesaler.order_service.clients.SubProductFeignClient;
import com.parimal.e_wholesaler.order_service.clients.WorkerFeignClient;
import com.parimal.e_wholesaler.order_service.dtos.*;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderItemDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderItemRequestDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderItemResponseDTO;
import com.parimal.e_wholesaler.order_service.entities.ShopOrderEntity;
import com.parimal.e_wholesaler.order_service.entities.ShopOrderItemEntity;
import com.parimal.e_wholesaler.common.exceptions.InvalidCalculationException;
import com.parimal.e_wholesaler.common.exceptions.MyException;
import com.parimal.e_wholesaler.common.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.order_service.repositories.ShopOrderItemRepository;
import com.parimal.e_wholesaler.order_service.utils.OrderStatus;
import com.parimal.e_wholesaler.order_service.utils.StockUpdate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ShopOrderItemService {

    private final ShopOrderItemRepository shopOrderItemRepository;

    private final ShopOrderService shopOrderService;

    private final ModelMapper modelMapper;

    private final SubProductFeignClient subProductFeignClient;
    private final WorkerFeignClient workerFeignClient;
    private final SalesFeignClient salesFeignClient;


    @Transactional
    public ShopOrderItemResponseDTO addOrderItem(HttpServletRequest request, ShopOrderItemRequestDTO requestDTO) {
        ShopOrderEntity shopOrder = shopOrderService.getShopOrderById(requestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order with id: " + requestDTO.getOrderId() + " not found."));
        OrderStatus orderStatus = shopOrder.getStatus();
        if(orderStatus.equals(OrderStatus.DELIVERED) || orderStatus.equals(OrderStatus.CONFIRMED)) {
            throw new RuntimeException("Order is already " + orderStatus + ", cannot add order-items.");
        }
        ApiResponse<ShopSubProductDTO> subProductResponse = subProductFeignClient.getShopSubProductByIds(
                requestDTO.getSubProductId(), requestDTO.getShopId());
        ShopSubProductDTO subProduct = subProductResponse.getData();
        if(subProduct == null) {
            throw new MyException(subProductResponse.getError());
        }
        if(subProduct.getStock() < requestDTO.getQuantity()) {
            throw new RuntimeException("Insufficient stock.");
        }
        Double totalPrice = subProduct.getSellingPrice() * requestDTO.getQuantity();
        if(!totalPrice.equals(requestDTO.getAmount())) {
            throw new InvalidCalculationException("Invalid calculation of amount.");
        }
        ShopOrderItemEntity toSave = new ShopOrderItemEntity();
        toSave.setAmount(requestDTO.getAmount());
        toSave.setQuantity(requestDTO.getQuantity());
        toSave.setSubProductId(requestDTO.getSubProductId());
        toSave.setShopOrder(shopOrder);
        ShopOrderItemEntity saved = shopOrderItemRepository.save(toSave);
        shopOrder.setAmount(shopOrder.getAmount() + saved.getAmount());
        shopOrderService.updateOrder(shopOrder);
        updateStockDetails(requestDTO.getShopId(), saved.getSubProductId(), saved.getQuantity(), StockUpdate.DECREASE);
        return modelMapper.map(saved, ShopOrderItemResponseDTO.class);
    }

    public ShopOrderItemDTO getOrderItemById(HttpServletRequest request, Long id) {
        ShopOrderItemEntity shopOrderItem = shopOrderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order item with id: " + id + " not found."));
        return modelMapper.map(shopOrderItem, ShopOrderItemDTO.class);
    }

    @Transactional
    public MessageDTO removeOrderItem(HttpServletRequest request, OrderItemDeleteRequestDTO requestDTO) {
        workerAndShopCheck(requestDTO.getShopId(), requestDTO.getWorkerId());
        ShopOrderItemEntity shopOrderItem = shopOrderItemRepository.findById(requestDTO.getOrderItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Order-item with id: " + requestDTO.getOrderItemId() + " not found."));
        ShopOrderEntity shopOrder = shopOrderItem.getShopOrder();
        if(shopOrder.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new RuntimeException("Order is delivered, cannot remove the order item.");
        }
        if(!shopOrderItem.getId().equals(requestDTO.getOrderItemId())) {
            throw new RuntimeException("Order-item doesn't belong to your shop.");
        }
        shopOrder.setAmount(shopOrder.getAmount() - shopOrderItem.getAmount());
        shopOrderService.updateOrder(shopOrder);
        shopOrderItemRepository.deleteById(shopOrderItem.getId());
        updateStockDetails(requestDTO.getShopId(), shopOrderItem.getSubProductId(), shopOrderItem.getQuantity(), StockUpdate.INCREASE);
        return new MessageDTO("Order-item deleted successfully.");
    }

    private void workerAndShopCheck(Long shopId, Long workerId) {
        ApiResponse<DataDTO<Boolean>> existenceResponse = workerFeignClient.workerExistsByIdAndShopId(workerId, shopId);
        if(existenceResponse.getData() == null) {
            throw new MyException(existenceResponse.getError());
        }
        if(!existenceResponse.getData().getData()) {
            throw new ResourceNotFoundException("Worker-Shop id mismatch or invalid worker id and shop id.");
        }
    }

    private void updateStockDetails(Long shopId, Long subProductId, Long stock, StockUpdate stockUpdate) {
        SubProductStockUpdateDTO updateDTO = new SubProductStockUpdateDTO();
        updateDTO.setShopId(shopId);
        updateDTO.setSubProductId(subProductId);
        updateDTO.setStock(stock);
        updateDTO.setStockUpdate(stockUpdate);
        ApiResponse<MessageDTO> stockUpdateResponse = subProductFeignClient.updateStock(updateDTO);
        if(stockUpdateResponse.getData() == null) {
            throw new MyException(stockUpdateResponse.getError());
        }
    }

}
