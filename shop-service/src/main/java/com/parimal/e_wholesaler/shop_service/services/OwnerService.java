package com.parimal.e_wholesaler.shop_service.services;

import com.parimal.e_wholesaler.shop_service.advices.ApiResponse;
import com.parimal.e_wholesaler.shop_service.clients.OrderFeignClient;
import com.parimal.e_wholesaler.shop_service.clients.SalesFeignClient;
import com.parimal.e_wholesaler.shop_service.clients.WorkerFeignClient;
import com.parimal.e_wholesaler.shop_service.dtos.*;
import com.parimal.e_wholesaler.shop_service.dtos.owner.*;
import com.parimal.e_wholesaler.shop_service.dtos.sales.SalesRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.sales.SalesResponseDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.ShopAndWorkersDTO;
import com.parimal.e_wholesaler.shop_service.entities.OwnerEntity;
import com.parimal.e_wholesaler.shop_service.entities.ShopEntity;
import com.parimal.e_wholesaler.shop_service.exceptions.MyException;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.shop_service.repositories.OwnerRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;

    private final WorkerFeignClient workerFeignClient;
    private final OrderFeignClient orderFeignClient;
    private final SalesFeignClient salesFeignClient;

    private final ModelMapper modelMapper;

    public OwnerResponseDTO createOwner(HttpServletRequest request, OwnerRequestDTO requestDTO) {
         boolean ownerExists = ownerRepository.existsByMobNo(Long.parseLong(requestDTO.getMobNo()));
         if(ownerExists) {
             throw new ResourceAlreadyExistsException("Owner already exists or use another mobile number");
         }
        OwnerEntity toSave = modelMapper.map(requestDTO, OwnerEntity.class);
        OwnerEntity saved = ownerRepository.save(toSave);
        return modelMapper.map(saved, OwnerResponseDTO.class);
    }

    public MessageDTO createDailySales(HttpServletRequest request, Long shopId) {
        ApiResponse<SalesResponseDTO> salesResponse = salesFeignClient.createSalesForShop(new SalesRequestDTO(shopId));
        log.error("Error: {}", salesResponse.getError());
        if(salesResponse.getError() != null) {
            throw new MyException(salesResponse.getError());
        }
        return new MessageDTO("Daily sales created for shop-id: " + salesResponse.getData().getId());
    }

    public List<ShopDTO> getShopsByOwnerId(HttpServletRequest request, Long ownerId) {
        OwnerEntity owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner with id: " + ownerId + " not found."));
        List<ShopDTO> shops = owner.getShops()
                .stream()
                .map(shop -> modelMapper.map(shop, ShopDTO.class))
                .toList();
        return shops;
    }

    public OwnerDTO getOwnerById(HttpServletRequest request, Long id) {
        OwnerEntity owner = ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner with id: " + id + " not found."));
        return modelMapper.map(owner, OwnerDTO.class);
    }

    public OwnerHomeDTO getHomeDetails(HttpServletRequest request, Long ownerId) {
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

    public List<ShopAndWorkersDTO> getShopWorkersByOwnerId(HttpServletRequest request, Long ownerId) {
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
            shopAndWorkersDTO.setShopName(shop.getName());
        }
        return shopWorkersList;
    }

    public MessageDTO deleteOwnerById(HttpServletRequest request, Long id) {
        boolean ownerExists = ownerRepository.existsById(id);
        if(!ownerExists) {
            throw new ResourceNotFoundException("Owner with id: " + id + " not found.");
        }
        ownerRepository.deleteById(id);
        return new MessageDTO("Customer deleted successfully.");
    }

//    Default type methods only accessible within the package.

    OwnerEntity getOwnerEntityById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner with id: " + id + " not found."));
    }

    boolean ownerExistsById(Long ownerId) {
        return ownerRepository.existsById(ownerId);
    }

    public Long getOwnerIdByMobNo(HttpServletRequest request, String mobNo) {
        return ownerRepository.findIdByMobNo(mobNo)
                .orElseThrow(() -> new ResourceNotFoundException("Owner with mobile number: " + mobNo + " not found."));
    }

}
