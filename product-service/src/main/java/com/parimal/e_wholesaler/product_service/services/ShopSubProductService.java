package com.parimal.e_wholesaler.product_service.services;

import com.parimal.e_wholesaler.product_service.advices.ApiResponse;
import com.parimal.e_wholesaler.product_service.clients.ShopFeignClient;
import com.parimal.e_wholesaler.product_service.dtos.DataDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductResponseDTO;
import com.parimal.e_wholesaler.product_service.entities.ProductEntity;
import com.parimal.e_wholesaler.product_service.entities.ShopSubProductEntity;
import com.parimal.e_wholesaler.product_service.entities.SubProductEntity;
import com.parimal.e_wholesaler.product_service.repositories.ShopSubProductRepository;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShopSubProductService {

    private final ShopSubProductRepository shopSubProductRepository;
    private final ProductService productService;
    private final SubProductService subProductService;
    private final ModelMapper modelMapper;
    private final ShopFeignClient shopFeignClient;



    @Transactional
    public ShopSubProductResponseDTO addShopSubProduct(ShopSubProductRequestDTO requestDTO) throws Exception {
        ApiResponse<DataDTO<Boolean>> shopExistsResponse = shopFeignClient.shopExistsById(requestDTO.getShopId());
        if(shopExistsResponse.getData() != null) {
            if(!shopExistsResponse.getData().getData()) {
                throw new ResourceNotFoundException("Shop with id: " + requestDTO.getShopId() + " not found.");
            }
            Optional<ProductEntity> product = productService.findByName(requestDTO.getProductName());
            ShopSubProductResponseDTO responseDTO = new ShopSubProductResponseDTO();
            HashMap<Long, Double> idToPriceMap = new HashMap<>();
            if(product.isEmpty()) {
                ProductEntity toSaveProduct = modelMapper.map(requestDTO, ProductEntity.class);
                ProductEntity savedProduct = productService.saveProduct(toSaveProduct);
                responseDTO.setProductId(savedProduct.getId());
                for (Double price : requestDTO.getPrices()) {
                    SubProductEntity savedSubProduct = subProductService.addSubProduct(savedProduct, price);
                    ShopSubProductEntity toSaveShopSubProduct = new ShopSubProductEntity();
                    toSaveShopSubProduct.setSubProduct(savedSubProduct);
                    toSaveShopSubProduct.setShopId(requestDTO.getShopId());
                    ShopSubProductEntity saved = shopSubProductRepository.save(toSaveShopSubProduct);
                    idToPriceMap.put(saved.getId(), savedSubProduct.getPrice());
                }
            }
            else {
                List<SubProductEntity> subProducts = product.get().getSubProducts();
                List<Double> subProductPrices = product.get()
                        .getSubProducts()
                        .stream()
                        .map(SubProductEntity::getPrice)
                        .toList();
                Long shopId = requestDTO.getShopId();
                responseDTO.setProductId(product.get().getId());
                requestDTO.getPrices()
                        .forEach(price -> {
                            boolean shouldAssignSubProduct = false;
                            SubProductEntity subProduct = null;
                            if(subProductPrices.contains(price)) {
                                int idx = subProductPrices.indexOf(price);
                                boolean shopHasNoSubProduct = subProducts.get(idx)
                                        .getShopSubProducts()
                                        .stream()
                                        .filter(s -> Objects.equals(s.getShopId(), shopId))
                                        .toList()
                                        .isEmpty();
                                if(shopHasNoSubProduct) {
                                    subProduct = subProducts.get(idx);
                                    shouldAssignSubProduct = true;
                                }
                            }
                            else {
                                subProduct = subProductService.addSubProduct(product.get(), price);
                                shouldAssignSubProduct = true;
                            }
                            if(shouldAssignSubProduct) {
                                ShopSubProductEntity toSaveShopSubProduct = new ShopSubProductEntity();
                                toSaveShopSubProduct.setShopId(requestDTO.getShopId());
                                toSaveShopSubProduct.setSubProduct(subProduct);
                                ShopSubProductEntity saved = shopSubProductRepository.save(toSaveShopSubProduct);
                                idToPriceMap.put(saved.getId(), subProduct.getPrice());
                            }
                        });
            }
            responseDTO.setIdToPriceMap(idToPriceMap);
            return responseDTO;
        }
        else{
            throw new Exception(shopExistsResponse.getError().getMessage());
        }
    }



}
