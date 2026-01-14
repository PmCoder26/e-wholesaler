package com.parimal.e_wholesaler.shop_service.services;

import com.parimal.e_wholesaler.shop_service.advices.ApiError;
import com.parimal.e_wholesaler.shop_service.advices.ApiResponse;
import com.parimal.e_wholesaler.shop_service.clients.OrderFeignClient;
import com.parimal.e_wholesaler.shop_service.clients.ProductFeignClient;
import com.parimal.e_wholesaler.shop_service.clients.SalesFeignClient;
import com.parimal.e_wholesaler.shop_service.clients.WorkerFeignClient;
import com.parimal.e_wholesaler.shop_service.dtos.DataDTO;
import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.PairDTO;
import com.parimal.e_wholesaler.shop_service.dtos.product.*;
import com.parimal.e_wholesaler.shop_service.dtos.sales.DailyRevenueDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopEditRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopResponseDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.ShopAndWorkersDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.WorkerDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.WorkerRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.worker.WorkerUpdateRequestDTO;
import com.parimal.e_wholesaler.shop_service.entities.OwnerEntity;
import com.parimal.e_wholesaler.shop_service.entities.ShopEntity;
import com.parimal.e_wholesaler.shop_service.exceptions.*;
import com.parimal.e_wholesaler.shop_service.repositories.ShopRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ShopService {

    private final ShopRepository shopRepository;
    private final OwnerService ownerService;

    private final ModelMapper modelMapper;

    private final ProductFeignClient productFeignClient;
    private final SalesFeignClient salesFeignClient;
    private final OrderFeignClient orderFeignClient;
    private final WorkerFeignClient workerFeignClient;


    public ShopResponseDTO createShop(HttpServletRequest request, Long ownerId, ShopRequestDTO requestDTO) {
         boolean shopExists = shopRepository.existsByNameAndGstNo(requestDTO.getName(), requestDTO.getGstNo());
         if(shopExists) {
             throw new ResourceAlreadyExistsException("Shop already exists.");
         }
        OwnerEntity owner = ownerService.getOwnerEntityById(ownerId);
        ShopEntity toSave = modelMapper.map(requestDTO, ShopEntity.class);
        toSave.setOwner(owner);
        ShopEntity saved = shopRepository.save(toSave);
        return modelMapper.map(saved, ShopResponseDTO.class);
    }

    public ShopDTO getShopById(HttpServletRequest request, Long id) {
        ShopEntity shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id: " + id + " not found."));
        return modelMapper.map(shop, ShopDTO.class);
    }

    public MessageDTO deleteShopById(HttpServletRequest request, Long id) {
        boolean exists = shopRepository.existsById(id);
        if(!exists) {
            throw new ResourceNotFoundException("Shop with id: " + id + " not found.");
        }
        shopRepository.deleteById(id);
        return new MessageDTO("Shop deleted successfully.");
    }

    public DataDTO<Boolean> existsById(HttpServletRequest request, Long id) {
        boolean shopExists = shopRepository.existsById(id);
        return new DataDTO<>(shopExists);
    }

    public ShopDTO updateShop(HttpServletRequest request, Long ownerId, ShopEditRequestDTO requestDTO) {
        ShopEntity shop = shopRepository.findById(requestDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id: " + requestDTO.getId() + " not found."));
        if(!shop.getOwner().getId().equals(ownerId))
            throw new UnAuthorizedAccessException(
                    "The shop with id " + ownerId + " doesn't belongs to you."
            );
        shop.setName(requestDTO.getName());
        shop.setGstNo(requestDTO.getGstNo());
        shop.setAddress(requestDTO.getAddress());
        shop.setCity(requestDTO.getCity());
        shop.setState(requestDTO.getState());
        ShopEntity saved = shopRepository.save(shop);
        return modelMapper.map(saved, ShopDTO.class);
    }

    public List<ShopProductDTO> getProductsByShopId(HttpServletRequest request, Long ownerId, Long shopId) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id: " + shopId + " now found."));
        if(!shop.getOwner().getId().equals(ownerId)) {
            ApiError apiError = ApiError.builder()
                    .message("You don't have permission to access this shop.")
                    .status(HttpStatus.FORBIDDEN)
                    .build();
            throw new MyException(apiError);
        }

        ApiResponse<List<ShopProductDTO>> productsResponse = productFeignClient.getShopSubProductsByShopId(shopId);
        if(productsResponse.getError() != null) {
            throw new MyException(productsResponse.getError());
        }
        return productsResponse.getData();
    }

    public List<DailyRevenueDTO> getDailyRevenue(HttpServletRequest request, Long ownerId) {
        boolean ownerExists = ownerService.ownerExistsById(ownerId);
        if(!ownerExists) throw new ResourceNotFoundException("Owner with id: " + ownerId + " not found.");
        List<ShopEntity> shopList = shopRepository.findByOwner_Id(ownerId);
        if(shopList.isEmpty()) return null;

        List<Long> shopIdList = shopList.stream()
                .map(ShopEntity::getId)
                .toList();

        ApiResponse<List<PairDTO<Long, Double>>> dailySalesResponse = salesFeignClient.getSalesByShopIdList(shopIdList);
        if(dailySalesResponse.getError() != null) throw new MyException(dailySalesResponse.getError());

        ApiResponse<List<PairDTO<Long, Long>>> dailyOrdersCountResponse = orderFeignClient.getDailyOrdersCountByShopIdList(shopIdList);
        if(dailyOrdersCountResponse.getError() != null)  throw new MyException(dailyOrdersCountResponse.getError());

        List<PairDTO<Long, Double>> salesList = dailySalesResponse.getData();
        List<PairDTO<Long, Long>> orderCountList = dailyOrdersCountResponse.getData();
        List<DailyRevenueDTO> revenueDTOList = new LinkedList<>();
        for (int x = 0; x < shopIdList.size(); x++) {
            PairDTO<Long, Double> sales = salesList.get(x);
            PairDTO<Long, Long> orderCount = orderCountList.get(x);
            ShopEntity shop = shopList.get(x);
            DailyRevenueDTO dailyRevenue = new DailyRevenueDTO(
                    shop.getName(),
                    shop.getCity(),
                    sales.getData2(),
                    orderCount.getData2());
            revenueDTOList.add(dailyRevenue);
        }
        return revenueDTOList;
    }

    public WorkerDTO addWorker(HttpServletRequest request, WorkerRequestDTO requestDTO) {
        boolean shopExists = shopRepository.existsById(requestDTO.getShopId());
        if(!shopExists) throw new ResourceNotFoundException("Shop with id: " + requestDTO.getShopId() + " not found.");

        ApiResponse<WorkerDTO> workerAddResponse = workerFeignClient.addWorker(requestDTO);
        if(workerAddResponse.getError() != null) throw new MyException(workerAddResponse.getError());

        return workerAddResponse.getData();
    }

    public WorkerDTO updateWorker(HttpServletRequest request, Long ownerId, WorkerUpdateRequestDTO requestDTO) {
        ShopEntity shop = shopRepository.findById(requestDTO.getShopId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id: " + requestDTO.getShopId() + "not found."));
        if(!shop.getOwner().getId().equals(ownerId)) throw new ForbiddenException("Permission for this shop worker denied.");

        ApiResponse<WorkerDTO> workerUpdateResponse = workerFeignClient.updateWorker(requestDTO);
        if(workerUpdateResponse.getError() != null) throw new MyException(workerUpdateResponse.getError());

        return workerUpdateResponse.getData();
    }

    public ShopSubProductResponseDTO addShopSubProduct(HttpServletRequest request, Long ownerId, ShopSubProductRequestDTO requestDTO) {
        ShopEntity shop = shopRepository.findById(requestDTO.getShopId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id: " + requestDTO.getShopId() + " not found."));
        if(!shop.getOwner().getId().equals(ownerId)) throw new ForbiddenException("Permission for this shop denied.");
        ApiResponse<ShopSubProductResponseDTO> responseDTO = productFeignClient.addShopSubProduct(requestDTO);
        if(responseDTO.getError() != null) throw new MyException(responseDTO.getError());

        return responseDTO.getData();
    }

    public MessageDTO removeShopSubProduct(HttpServletRequest request, Long ownerId, RequestDTO requestDTO) {
        ShopEntity shop = shopRepository.findById(requestDTO.getShopId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id: " + requestDTO.getShopId() + " not found."));
        if(!shop.getOwner().getId().equals(ownerId)) throw new ForbiddenException("Permission for this shop-sub-product denied.");
        ApiResponse<MessageDTO> shopSubProductResponse = productFeignClient.removeShopSubProduct(requestDTO);
        if(shopSubProductResponse.getError() != null) throw new MyException(shopSubProductResponse.getError());

        return shopSubProductResponse.getData();
    }

    public MessageDTO updateShopSubProduct(HttpServletRequest request, Long ownerId, ShopSubProductUpdateRequestDTO requestDTO) {
        boolean shopExists = shopRepository.existsByIdAndOwner_Id(requestDTO.getShopId(), ownerId);
        if(!shopExists) throw new ResourceNotFoundException("Shop of the requested owner not found.");
        ApiResponse<MessageDTO> updateResponse = productFeignClient.updateShopSubProduct(requestDTO);
        if(updateResponse.getError() != null) throw new MyException(updateResponse.getError());

        return updateResponse.getData();
    }

    public MessageDTO removeProductByShopIdAndProductName(HttpServletRequest request, Long ownerId, ProductRemoveRequestDTO requestDTO) {
        boolean shopExists = shopRepository.existsByIdAndOwner_Id(requestDTO.getShopId(), ownerId);
        if(!shopExists) throw new ResourceNotFoundException("Shop of the requested owner not found.");

        ApiResponse<MessageDTO> removalResponse = productFeignClient.removeProductByShopIdAndProductName(requestDTO);
        if(removalResponse.getError() != null) throw new MyException(removalResponse.getError());

        return removalResponse.getData();
    }

    public ShopAndWorkersDTO getWorkersByShopId(HttpServletRequest request, Long ownerId, Long shopId) {
        boolean shopExists = shopRepository.existsByIdAndOwner_Id(shopId, ownerId);
        if(!shopExists) throw new ResourceNotFoundException("Shop of the requested owner not found.");

        ApiResponse<ShopAndWorkersDTO> workersResponse = workerFeignClient.getWorkersByShopId(shopId);
        if(workersResponse.getError() != null) throw new MyException(workersResponse.getError());

        return workersResponse.getData();
    }
}
