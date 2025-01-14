package com.parimal.e_wholesaler.product_service.services;

import com.parimal.e_wholesaler.product_service.advices.ApiResponse;
import com.parimal.e_wholesaler.product_service.clients.ShopFeignClient;
import com.parimal.e_wholesaler.product_service.dtos.DataDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.RequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductResponseDTO;
import com.parimal.e_wholesaler.product_service.entities.ProductEntity;
import com.parimal.e_wholesaler.product_service.entities.ShopSubProductEntity;
import com.parimal.e_wholesaler.product_service.entities.SubProductEntity;
import com.parimal.e_wholesaler.product_service.repositories.ShopSubProductRepository;
import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

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
        if (shopExistsResponse.getData() != null) {
            if (!shopExistsResponse.getData().getData()) {
                throw new ResourceNotFoundException("Shop with id: " + requestDTO.getShopId() + " not found.");
            }
            Optional<ProductEntity> product = productService.findByName(requestDTO.getProductName());
            ShopSubProductResponseDTO responseDTO = new ShopSubProductResponseDTO();
            HashMap<Long, Double> idToPriceMap = new HashMap<>();
            HashMap<Double, Double> map = requestDTO.getMrpToSelling();
            Set<Double> MRPs = map.keySet();
            if (product.isEmpty()) {
                ProductEntity toSaveProduct = modelMapper.map(requestDTO, ProductEntity.class);
                ProductEntity savedProduct = productService.saveProduct(toSaveProduct);
                responseDTO.setProductId(savedProduct.getId());
                for (Double Mrp : MRPs) {
                    SubProductEntity savedSubProduct = subProductService.addSubProduct(savedProduct, Mrp);
                    ShopSubProductEntity toSaveShopSubProduct = new ShopSubProductEntity();
                    toSaveShopSubProduct.setSubProduct(savedSubProduct);
                    toSaveShopSubProduct.setShopId(requestDTO.getShopId());
                    toSaveShopSubProduct.setSellingPrice(map.get(Mrp));
                    ShopSubProductEntity saved = shopSubProductRepository.save(toSaveShopSubProduct);
                    idToPriceMap.put(saved.getId(), savedSubProduct.getMrp());
                }
            } else {
                List<SubProductEntity> subProducts = product.get().getSubProducts();
                List<Double> subProductPrices = product.get()
                        .getSubProducts()
                        .stream()
                        .map(SubProductEntity::getMrp)
                        .toList();
                Long shopId = requestDTO.getShopId();
                responseDTO.setProductId(product.get().getId());
                MRPs
                        .forEach(Mrp -> {
                            boolean shouldAssignSubProduct = false;
                            SubProductEntity subProduct = null;
                            if (subProductPrices.contains(Mrp)) {
                                int idx = subProductPrices.indexOf(Mrp);
                                boolean shopHasNoSubProduct = subProducts.get(idx)
                                        .getShopSubProducts()
                                        .stream()
                                        .filter(s -> Objects.equals(s.getShopId(), shopId))
                                        .toList()
                                        .isEmpty();
                                if (shopHasNoSubProduct) {
                                    subProduct = subProducts.get(idx);
                                    shouldAssignSubProduct = true;
                                }
                            } else {
                                subProduct = subProductService.addSubProduct(product.get(), Mrp);
                                shouldAssignSubProduct = true;
                            }
                            if (shouldAssignSubProduct) {
                                ShopSubProductEntity toSaveShopSubProduct = new ShopSubProductEntity();
                                toSaveShopSubProduct.setShopId(requestDTO.getShopId());
                                toSaveShopSubProduct.setSubProduct(subProduct);
                                toSaveShopSubProduct.setSellingPrice(map.get(Mrp));
                                ShopSubProductEntity saved = shopSubProductRepository.save(toSaveShopSubProduct);
                                idToPriceMap.put(saved.getId(), subProduct.getMrp());
                            }
                        });
            }
            responseDTO.setIdToPriceMap(idToPriceMap);
            return responseDTO;
        } else {
            throw new Exception(shopExistsResponse.getError().getMessage());
        }
    }

    public ShopSubProductDTO getShopSubProductById(RequestDTO requestDTO) throws Exception {
        ApiResponse<DataDTO<Boolean>> shopExistsData = shopFeignClient.shopExistsById(requestDTO.getShopId());
        if(shopExistsData.getData() == null || !shopExistsData.getData().getData()) {
            throw new Exception(shopExistsData.getError().getMessage());
        }
        ShopSubProductEntity shopSubProduct = shopSubProductRepository.findById(requestDTO.getShopSubProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop sub-product with id: " + requestDTO.getShopSubProductId() + " not found."));
        if(shopSubProduct.getShopId() != requestDTO.getShopId()) {
            throw new Exception("Requested shop sub-product is not owned by your shop.");
        }
        ShopSubProductDTO shopSubProductDTO = modelMapper.map(shopSubProduct, ShopSubProductDTO.class);
        shopSubProductDTO.setMrp(shopSubProduct.getSubProduct().getMrp());
        return shopSubProductDTO;
    }

    public MessageDTO removeShopSubProductById(RequestDTO requestDTO) throws Exception {
        ApiResponse<DataDTO<Boolean>> shopExistsData = shopFeignClient.shopExistsById(requestDTO.getShopId());
        if(shopExistsData.getData() == null || !shopExistsData.getData().getData()) {
            throw new Exception(shopExistsData.getError().getMessage());
        }
        ShopSubProductEntity shopSubProduct = shopSubProductRepository.findById(requestDTO.getShopSubProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop sub-product with id: " + requestDTO.getShopSubProductId() + " not found."));
        if(shopSubProduct.getShopId() != requestDTO.getShopId()) {
            throw new Exception("Requested shop sub-product is not owned by your shop.");
        }
        shopSubProductRepository.deleteById(requestDTO.getShopSubProductId());
        return new MessageDTO("Shop sub-product removed successfully");
    }


}
