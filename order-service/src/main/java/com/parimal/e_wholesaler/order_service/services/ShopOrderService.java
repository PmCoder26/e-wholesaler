package com.parimal.e_wholesaler.order_service.services;

import com.parimal.e_wholesaler.order_service.advices.ApiResponse;
import com.parimal.e_wholesaler.order_service.clients.WorkerFeignClient;
import com.parimal.e_wholesaler.order_service.dtos.DataDTO;
import com.parimal.e_wholesaler.order_service.dtos.DeleteRequestDTO;
import com.parimal.e_wholesaler.order_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderRequestDTO;
import com.parimal.e_wholesaler.order_service.dtos.shop.ShopOrderResponseDTO;
import com.parimal.e_wholesaler.order_service.entities.ShopOrderEntity;
import com.parimal.e_wholesaler.order_service.exceptions.MyException;
import com.parimal.e_wholesaler.order_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.order_service.repositories.ShopOrderRepository;
import com.parimal.e_wholesaler.order_service.utils.OrderStatus;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class ShopOrderService {

    private final ShopOrderRepository shopOrderRepository;
    private final WorkerFeignClient workerFeignClient;
    private final ModelMapper modelMapper;

    public ShopOrderResponseDTO createOrder(ShopOrderRequestDTO requestDTO) {
        shopAndWorkerCheck(requestDTO.getWorkerId(), requestDTO.getShopId());
        ShopOrderEntity toSave = new ShopOrderEntity();
        toSave.setWorkerId(requestDTO.getWorkerId());
        toSave.setStatus(OrderStatus.CREATING);
        ShopOrderEntity saved = shopOrderRepository.save(toSave);
        ShopOrderResponseDTO responseDTO = new ShopOrderResponseDTO();
        responseDTO.setOrderId(saved.getId());
        return responseDTO;
    }

    public ShopOrderDTO getOrderById(Long id) {
        ShopOrderEntity shopOrder = shopOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop-order with id: " + id + " not found."));
        return modelMapper.map(shopOrder, ShopOrderDTO.class);
    }

    public MessageDTO deleteOrderById(DeleteRequestDTO requestDTO) {
        shopAndWorkerCheck(requestDTO.getWorkerId(), requestDTO.getShopId());
        ShopOrderEntity shopOrder = shopOrderRepository.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order with id: " + requestDTO.getOrderId() + " not found."));
        if(Objects.equals(shopOrder.getStatus(), OrderStatus.DELIVERED)) {
            throw new RuntimeException("Ordered cannot be deleted as it is delivered.");
        }
        shopOrderRepository.delete(shopOrder);
        return new MessageDTO("Order deleted successfully.");
    }


    boolean existsById(Long id) {
        return shopOrderRepository.existsById(id);
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

}
