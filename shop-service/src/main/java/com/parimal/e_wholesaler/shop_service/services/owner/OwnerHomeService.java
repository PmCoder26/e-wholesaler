package com.parimal.e_wholesaler.shop_service.services.owner;

import com.parimal.e_wholesaler.common.advices.ApiResponse;
import com.parimal.e_wholesaler.shop_service.clients.OrderFeignClient;
import com.parimal.e_wholesaler.shop_service.clients.SalesFeignClient;
import com.parimal.e_wholesaler.shop_service.clients.WorkerFeignClient;
import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerHomeDTO;
import com.parimal.e_wholesaler.shop_service.entities.OwnerEntity;
import com.parimal.e_wholesaler.shop_service.entities.ShopEntity;
import com.parimal.e_wholesaler.common.exceptions.MyException;
import com.parimal.e_wholesaler.common.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.shop_service.repositories.OwnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OwnerHomeService {

    private final OwnerRepository ownerRepository;

    private final WorkerFeignClient workerFeignClient;
    private final OrderFeignClient orderFeignClient;
    private final SalesFeignClient salesFeignClient;


    public OwnerHomeDTO getHomeDetails(Long ownerId) {
        OwnerEntity owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner with id: " + ownerId + " not found."));
        List<ShopEntity> shops = owner.getShops();
        Integer shopCount = shops.size();
        List<Long> shopIdList = shops.stream()
                .map(ShopEntity::getId)
                .toList();

        ApiResponse<Long> workerCountResponse = workerFeignClient.getWorkerCount(shopIdList);
        ApiResponse<Long> orderCountResponse = orderFeignClient.getOrderCount(shopIdList);
        ApiResponse<Double> salesAmountResponse = salesFeignClient.getSalesAmount(shopIdList);

        if(workerCountResponse.getError() != null) {
            throw new MyException(workerCountResponse.getError());
        }
        else if(orderCountResponse.getError() != null) {
            throw new MyException(orderCountResponse.getError());
        }
        else if(salesAmountResponse.getError() != null) {
            throw new MyException(salesAmountResponse.getError());
        }
        return new OwnerHomeDTO(
                shopCount,
                orderCountResponse.getData(),
                workerCountResponse.getData(),
                salesAmountResponse.getData()
        );
    }

}
