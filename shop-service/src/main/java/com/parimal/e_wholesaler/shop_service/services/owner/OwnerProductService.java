package com.parimal.e_wholesaler.shop_service.services.owner;

import com.parimal.e_wholesaler.common.advices.ApiError;
import com.parimal.e_wholesaler.common.advices.ApiResponse;
import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopRequestDTO;
import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopResponseDTO;
import com.parimal.e_wholesaler.common.dtos.product.ProductIdentityDTO;
import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitDTO;
import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitRequestDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.AddSubProductsForShopRequestDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.AddSubProductsForShopResponseDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.SubProductDTO2;
import com.parimal.e_wholesaler.common.exceptions.MyException;
import com.parimal.e_wholesaler.common.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.shop_service.clients.ProductFeignClient;
import com.parimal.e_wholesaler.shop_service.clients.ShopSubProductFeignClient;
import com.parimal.e_wholesaler.shop_service.entities.ShopEntity;
import com.parimal.e_wholesaler.shop_service.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class OwnerProductService {

    private final ShopRepository shopRepository;

    private final ShopSubProductFeignClient shopSubProductFeignClient;
    private final ProductFeignClient productFeignClient;


    public AddProductForShopResponseDTO addProduct(Long ownerId, Long shopId, AddProductForShopRequestDTO requestDTO) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id: " + shopId + " now found."));
        if(!shop.getOwner().getId().equals(ownerId)) {
            ApiError apiError = ApiError.builder()
                    .message("You don't have permission to access this shop.")
                    .status(HttpStatus.FORBIDDEN)
                    .build();
            throw new MyException(apiError);
        }

        ApiResponse<AddProductForShopResponseDTO> productsResponse = productFeignClient.addProduct(shopId, requestDTO);
        if(productsResponse.getError() != null) throw new MyException(productsResponse.getError());

        return productsResponse.getData();
    }

    public List<ProductIdentityDTO> getShopProducts(Long ownerId, Long shopId) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id: " + shopId + " now found."));
        if(!shop.getOwner().getId().equals(ownerId)) {
            ApiError apiError = ApiError.builder()
                    .message("You don't have permission to access this shop.")
                    .status(HttpStatus.FORBIDDEN)
                    .build();
            throw new MyException(apiError);
        }

        ApiResponse<List<ProductIdentityDTO>> productsResponse = productFeignClient.getShopProductForOwner(shopId);
        if(productsResponse.getError() != null) {
            throw new MyException(productsResponse.getError());
        }
        return productsResponse.getData();
    }

    public AddSubProductsForShopResponseDTO addShopSubProduct(Long ownerId, Long shopId, Long productId, AddSubProductsForShopRequestDTO requestDTO) {
        shopExistsByShopIdAndOwnerId(shopId, ownerId);

        ApiResponse<AddSubProductsForShopResponseDTO> responseDTO = shopSubProductFeignClient.addShopSubProduct(shopId, productId, requestDTO);
        if(responseDTO.getError() != null) throw new MyException(responseDTO.getError());

        return responseDTO.getData();
    }

    public List<SubProductDTO2> getShopProductDetails(Long ownerId, Long shopId, Long productId) {
        shopExistsByShopIdAndOwnerId(shopId, ownerId);

        ApiResponse<List<SubProductDTO2>> response = shopSubProductFeignClient.getShopProductDetails(shopId, productId);
        if(response.getError() != null) throw new MyException(response.getError());

        return response.getData();
    }

    public void deleteShopSubProduct(Long ownerId, Long shopId, Long shopSubProductId) {
        shopExistsByShopIdAndOwnerId(shopId, ownerId);

        ApiResponse<Void> response = shopSubProductFeignClient.deleteShopSubProduct(shopId, shopSubProductId);
        if(response!= null && response.getError() != null) throw new MyException(response.getError());
    }

    public SellingUnitDTO updateProductSellingUnit(
            Long ownerId, Long shopId, Long shopSubProductId, Long sellingUnitId, SellingUnitRequestDTO requestDTO)
    {
        if(requestDTO == null) throw new IllegalArgumentException("Update-data cannot be empty");

        shopExistsByShopIdAndOwnerId(shopId, ownerId);

        ApiResponse<SellingUnitDTO> response = shopSubProductFeignClient
                .updateProductSellingUnit(shopId, shopSubProductId, sellingUnitId, requestDTO);
        if(response.getError() != null) throw new MyException(response.getError());

        return response.getData();
    }

    public void deleteProductSellingUnit(Long ownerId, Long shopId, Long shopSubProductId, Long sellingUnitId) {
        shopExistsByShopIdAndOwnerId(shopId, ownerId);

        ApiResponse<Void> response = shopSubProductFeignClient.deleteProductSellingUnit(shopId, shopSubProductId, sellingUnitId);
        if(response != null && response.getError() != null) throw new MyException(response.getError());
    }

    private void shopExistsByShopIdAndOwnerId(Long shopId, Long ownerId) {
        if(!shopRepository.existsByIdAndOwner_Id(shopId, ownerId))
            throw new ResourceNotFoundException("Shop with id: " + shopId + " not found or permission for this shop denied.");
    }

    public void deleteShopProduct(Long ownerId, Long shopId, Long productId) {
        shopExistsByShopIdAndOwnerId(shopId, ownerId);

        ApiResponse<Void> response = productFeignClient.deleteShopProduct(shopId, productId);
        if(response != null && response.getError() != null) throw new MyException(response.getError());
    }
}
