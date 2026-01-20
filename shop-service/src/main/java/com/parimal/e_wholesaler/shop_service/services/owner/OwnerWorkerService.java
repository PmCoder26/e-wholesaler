package com.parimal.e_wholesaler.shop_service.services.owner;

import com.parimal.e_wholesaler.common.advices.ApiResponse;
import com.parimal.e_wholesaler.shop_service.clients.WorkerFeignClient;
import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.*;
import com.parimal.e_wholesaler.shop_service.entities.OwnerEntity;
import com.parimal.e_wholesaler.shop_service.entities.ShopEntity;
import com.parimal.e_wholesaler.common.exceptions.MyException;
import com.parimal.e_wholesaler.common.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.shop_service.repositories.OwnerRepository;
import com.parimal.e_wholesaler.shop_service.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OwnerWorkerService {

    private final ShopRepository shopRepository;
    private final OwnerRepository ownerRepository;

    private final WorkerFeignClient workerFeignClient;


    public WorkerDTO addWorker(Long ownerId, WorkerRequestDTO requestDTO) {
        shopExistsByShopIdAndOwnerId(requestDTO.getShopId(), ownerId);

        ApiResponse<WorkerDTO> workerAddResponse = workerFeignClient.addWorker(requestDTO);
        if(workerAddResponse.getError() != null) throw new MyException(workerAddResponse.getError());

        return workerAddResponse.getData();
    }

    public WorkerDTO updateWorker(Long ownerId, WorkerUpdateRequestDTO requestDTO) {
        shopExistsByShopIdAndOwnerId(requestDTO.getShopId(), ownerId);

        ApiResponse<WorkerDTO> workerUpdateResponse = workerFeignClient.updateWorker(requestDTO);
        if(workerUpdateResponse.getError() != null) throw new MyException(workerUpdateResponse.getError());

        return workerUpdateResponse.getData();
    }

    public MessageDTO deleteWorkerById(Long ownerId, WorkerDeleteRequestDTO requestDTO) {
        shopExistsByShopIdAndOwnerId(requestDTO.getShopId(), ownerId);

        ApiResponse<MessageDTO> response = workerFeignClient.deleteWorkerById(requestDTO);
        if(response.getError() != null) throw new MyException(response.getError());
        return response.getData();
    }

    public ShopAndWorkersDTO getWorkersByShopId(Long ownerId, Long shopId) {
        shopExistsByShopIdAndOwnerId(shopId, ownerId);

        ApiResponse<ShopAndWorkersDTO> workersResponse = workerFeignClient.getWorkersByShopId(shopId);
        if(workersResponse.getError() != null) throw new MyException(workersResponse.getError());

        return workersResponse.getData();
    }

    public List<ShopAndWorkersDTO> getShopWorkersByOwnerId(Long ownerId) {
        OwnerEntity owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner with id: " + ownerId + " not found."));
        List<ShopEntity> shops = owner.getShops();
        List<Long> shopIdList = shops.stream()
                .map(ShopEntity::getId)
                .toList();

        ApiResponse<List<ShopAndWorkersDTO>> shopWorkersResponse = workerFeignClient.getWorkersByShopIdList(shopIdList);
        List<ShopAndWorkersDTO> shopWorkersList = shopWorkersResponse.getData();
        if(shopWorkersResponse.getError() != null) throw new MyException(shopWorkersResponse.getError());

        for (int x = 0; x < shops.size(); x++) {
            ShopEntity shop = shops.get(x);
            ShopAndWorkersDTO shopAndWorkersDTO = shopWorkersList.get(x);
            if(!shop.getId().equals(shopAndWorkersDTO.getShopId())) throw new RuntimeException("Shop-id sequence mismatched.");
        }
        return shopWorkersList;
    }

    private void shopExistsByShopIdAndOwnerId(Long shopId, Long ownerId) {
        if(!shopRepository.existsByIdAndOwner_Id(shopId, ownerId))
            throw new ResourceNotFoundException("Shop with id: " + shopId + " not found or permission for this shop denied.");
    }

}
