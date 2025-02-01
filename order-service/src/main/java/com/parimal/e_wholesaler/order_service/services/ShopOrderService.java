package com.parimal.e_wholesaler.order_service.services;

import com.parimal.e_wholesaler.order_service.advices.ApiResponse;
import com.parimal.e_wholesaler.order_service.clients.SalesFeignClient;
import com.parimal.e_wholesaler.order_service.clients.SubProductFeignClient;
import com.parimal.e_wholesaler.order_service.clients.WorkerFeignClient;
import com.parimal.e_wholesaler.order_service.dtos.*;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderRequestDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderResponseDTO;
import com.parimal.e_wholesaler.order_service.entities.ShopOrderEntity;
import com.parimal.e_wholesaler.order_service.entities.ShopOrderItemEntity;
import com.parimal.e_wholesaler.order_service.exceptions.MyException;
import com.parimal.e_wholesaler.order_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.order_service.repositories.ShopOrderRepository;
import com.parimal.e_wholesaler.order_service.utils.OrderStatus;
import com.parimal.e_wholesaler.order_service.utils.SalesUpdate;
import com.parimal.e_wholesaler.order_service.utils.StockUpdate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@AllArgsConstructor
public class ShopOrderService {

    private final ShopOrderRepository shopOrderRepository;

    private final WorkerFeignClient workerFeignClient;
    private final SalesFeignClient salesFeignClient;
    private final SubProductFeignClient subProductFeignClient;

    private final ModelMapper modelMapper;

    @Transactional
    public ShopOrderResponseDTO createOrder(HttpServletRequest request, ShopOrderRequestDTO requestDTO) {
        shopAndWorkerCheck(requestDTO.getWorkerId(), requestDTO.getShopId());
        ShopOrderEntity toSave = new ShopOrderEntity();
        toSave.setShopId(requestDTO.getShopId());
        toSave.setWorkerId(requestDTO.getWorkerId());
        toSave.setStatus(OrderStatus.CREATING);
        ShopOrderEntity saved = shopOrderRepository.save(toSave);
        ShopOrderResponseDTO responseDTO = new ShopOrderResponseDTO();
        responseDTO.setOrderId(saved.getId());
        return responseDTO;
    }

    public ShopOrderDTO getOrderById(HttpServletRequest request, Long id) {
        ShopOrderEntity shopOrder = shopOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop-order with id: " + id + " not found."));
        return modelMapper.map(shopOrder, ShopOrderDTO.class);
    }

    public Long calculateShopCount(HttpServletRequest request, List<Long> shopIdList) {
        AtomicLong orderCount = new AtomicLong();
        shopIdList
                .forEach(shopId -> {
                    Optional<Long> count = Optional.of(orderCount.addAndGet(shopOrderRepository.countByShopId(shopId)));
                    if(count.isEmpty()) {
                        
                    }
                });
        return orderCount.get();
    }

    @Transactional
    public MessageDTO deleteOrderById(HttpServletRequest request, DeleteRequestDTO requestDTO) {
        shopAndWorkerCheck(requestDTO.getWorkerId(), requestDTO.getShopId());
        ShopOrderEntity shopOrder = shopOrderRepository.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order with id: " + requestDTO.getOrderId() + " not found."));
        if(Objects.equals(shopOrder.getStatus(), OrderStatus.DELIVERED)) {
            throw new RuntimeException("Ordered cannot be deleted as it is delivered.");
        }
        if(!shopOrder.getWorkerId().equals(requestDTO.getWorkerId())) {
            throw new RuntimeException("This order doesn't belong to you.");
        }
        updateStockDetails(requestDTO.getShopId(), shopOrder.getShopOrderItems());
        shopOrderRepository.delete(shopOrder);
        return new MessageDTO("Order deleted successfully.");
    }

    @Transactional
    public MessageDTO updateOrderStatus(HttpServletRequest request, ShopOrderStatusUpdateDTO requestDTO) {
        shopAndWorkerCheck(requestDTO.getWorkerId(), requestDTO.getShopId());
        ShopOrderEntity shopOrder = shopOrderRepository.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order with id: " + requestDTO.getOrderId() + " not found."));
        OrderStatus orderStatus = shopOrder.getStatus();
        if(!shopOrder.getWorkerId().equals(requestDTO.getWorkerId())) {
            throw new RuntimeException("This order doesn't belong to you.");
        }
        if(orderStatus.equals(requestDTO.getStatus())) {
            throw new RuntimeException("The order status already " + requestDTO.getStatus());
        }
        else if(orderStatus.equals(OrderStatus.DELIVERED)) {
            throw new RuntimeException("The order status is already 'DELIVERED' hence status cannot be changed.");
        }
        else if(requestDTO.getStatus().equals(OrderStatus.CONFIRMED)) {
            if(requestDTO.getPaymentMethod() == null) {
                throw new RuntimeException("Please provide the payment method to confirm order.");
            }
            shopOrder.setPayment(requestDTO.getPaymentMethod());
        }
        shopOrder.setStatus(requestDTO.getStatus());
        ShopOrderEntity saved = shopOrderRepository.save(shopOrder);
        if(saved.getStatus().equals(OrderStatus.DELIVERED)) {
            updateSalesDetails(requestDTO.getShopId(), saved.getAmount());
        }
        return new MessageDTO("Order status updated successfully.");
    }

    private void shopAndWorkerCheck(Long workerId, Long shopId) {
        ApiResponse<DataDTO<Boolean>> existenceResponse = workerFeignClient.workerExistsByIdAndShopId(workerId, shopId);
        if(existenceResponse.getData() == null) {
            throw new MyException(existenceResponse.getError());
        }
        if(!existenceResponse.getData().getData()) {
            throw new ResourceNotFoundException("Worker-Shop id mismatch or invalid worker id and shop id.");
        }
    }

    private void updateSalesDetails(Long shopId, Double amount) {
        SalesUpdateRequestDTO requestDTO = new SalesUpdateRequestDTO();
        requestDTO.setShopId(shopId);
        requestDTO.setAmount(amount);
        requestDTO.setUpdateMode(SalesUpdate.CREDIT);
        ApiResponse<MessageDTO> salesUpdateResponse = salesFeignClient.updateDailySales(requestDTO);
        if(salesUpdateResponse.getData() == null) {
            throw new MyException(salesUpdateResponse.getError());
        }
    }

    private void updateStockDetails(Long shopId, List<ShopOrderItemEntity> orderItems) {
        orderItems.forEach( orderItem -> {
            updateStockDetailsForEach(shopId, orderItem.getSubProductId(), orderItem.getQuantity());
        });
    }

    private void updateStockDetailsForEach(Long shopId, Long subProductId, Long stock) {
        SubProductStockUpdateDTO updateDTO = new SubProductStockUpdateDTO();
        updateDTO.setShopId(shopId);
        updateDTO.setSubProductId(subProductId);
        updateDTO.setStock(stock);
        updateDTO.setStockUpdate(StockUpdate.INCREASE);
        ApiResponse<MessageDTO> stockUpdateResponse = subProductFeignClient.updateStock(updateDTO);
        if(stockUpdateResponse.getData() == null) {
            throw new MyException(stockUpdateResponse.getError());
        }
    }

    boolean existsById(Long id) {
        return shopOrderRepository.existsById(id);
    }

    Optional<ShopOrderEntity> getShopOrderById(Long id) {
        return shopOrderRepository.findById(id);
    }

    void updateOrder(ShopOrderEntity shopOrder) {
        shopOrderRepository.save(shopOrder);
    }

}
