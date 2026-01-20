package com.parimal.e_wholesaler.shop_service.services.owner;

import com.parimal.e_wholesaler.shop_service.advices.ApiError;
import com.parimal.e_wholesaler.shop_service.advices.ApiResponse;
import com.parimal.e_wholesaler.shop_service.clients.ProductFeignClient;
import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.product.*;
import com.parimal.e_wholesaler.shop_service.entities.ShopEntity;
import com.parimal.e_wholesaler.shop_service.exceptions.MyException;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.shop_service.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OwnerProductService {

    private final ShopRepository shopRepository;

    private final ProductFeignClient productFeignClient;


    public List<ShopProductDTO> getProductsByShopId(Long ownerId, Long shopId) {
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

    public ShopSubProductResponseDTO addShopSubProduct(Long ownerId, ShopSubProductRequestDTO requestDTO) {
        shopExistsByShopIdAndOwnerId(requestDTO.getShopId(), ownerId);

        ApiResponse<ShopSubProductResponseDTO> responseDTO = productFeignClient.addShopSubProduct(requestDTO);
        if(responseDTO.getError() != null) throw new MyException(responseDTO.getError());

        return responseDTO.getData();
    }

    public MessageDTO removeShopSubProduct(Long ownerId, RequestDTO requestDTO) {
        shopExistsByShopIdAndOwnerId(requestDTO.getShopId(), ownerId);

        ApiResponse<MessageDTO> shopSubProductResponse = productFeignClient.removeShopSubProduct(requestDTO);
        if(shopSubProductResponse.getError() != null) throw new MyException(shopSubProductResponse.getError());

        return shopSubProductResponse.getData();
    }

    public MessageDTO updateShopSubProduct(Long ownerId, ShopSubProductUpdateRequestDTO requestDTO) {
        shopExistsByShopIdAndOwnerId(requestDTO.getShopId(), ownerId);

        ApiResponse<MessageDTO> updateResponse = productFeignClient.updateShopSubProduct(requestDTO);
        if(updateResponse.getError() != null) throw new MyException(updateResponse.getError());

        return updateResponse.getData();
    }

    public MessageDTO removeProductByShopIdAndProductName(Long ownerId, ProductRemoveRequestDTO requestDTO) {
        shopExistsByShopIdAndOwnerId(requestDTO.getShopId(), ownerId);

        ApiResponse<MessageDTO> removalResponse = productFeignClient.removeProductByShopIdAndProductName(requestDTO);
        if(removalResponse.getError() != null) throw new MyException(removalResponse.getError());

        return removalResponse.getData();
    }

    private void shopExistsByShopIdAndOwnerId(Long shopId, Long ownerId) {
        if(!shopRepository.existsByIdAndOwner_Id(shopId, ownerId))
            throw new ResourceNotFoundException("Shop with id: " + shopId + " not found or permission for this shop denied.");
    }

}
