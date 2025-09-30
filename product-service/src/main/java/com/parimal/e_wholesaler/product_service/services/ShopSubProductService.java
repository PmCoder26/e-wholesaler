package com.parimal.e_wholesaler.product_service.services;

import com.parimal.e_wholesaler.product_service.advices.ApiResponse;
import com.parimal.e_wholesaler.product_service.clients.ShopFeignClient;
import com.parimal.e_wholesaler.product_service.dtos.*;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.*;
import com.parimal.e_wholesaler.product_service.entities.ProductEntity;
import com.parimal.e_wholesaler.product_service.entities.ShopSubProductEntity;
import com.parimal.e_wholesaler.product_service.entities.SubProductEntity;
import com.parimal.e_wholesaler.product_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.product_service.repositories.ShopSubProductRepository;
import com.parimal.e_wholesaler.product_service.utils.StockUpdate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class ShopSubProductService {

    private final ShopSubProductRepository shopSubProductRepository;
    private final ProductService productService;
    private final SubProductService subProductService;
    private final ModelMapper modelMapper;
    private final ShopFeignClient shopFeignClient;


    @Transactional
    public ShopSubProductResponseDTO addShopSubProduct(HttpServletRequest request, ShopSubProductRequestDTO requestDTO) throws Exception {
        ApiResponse<DataDTO<Boolean>> shopExistsResponse = shopFeignClient.shopExistsById(requestDTO.getShopId());
        if (shopExistsResponse.getData() != null) {
            if (!shopExistsResponse.getData().getData()) {
                throw new ResourceNotFoundException("Shop with id: " + requestDTO.getShopId() + " not found.");
            }
            Optional<ProductEntity> product = productService.findByName(requestDTO.getProductName());
            ShopSubProductResponseDTO responseDTO = new ShopSubProductResponseDTO();
            HashMap<Long, Double> idToPriceMap = new HashMap<>();
            HashMap<Double, QuantityToSellingPrice> map = requestDTO.getMrpToSelling();
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
                    toSaveShopSubProduct.setSellingPrice(map.get(Mrp).getSellingPrice());
                    toSaveShopSubProduct.setQuantity(map.get(Mrp).getQuantity());
                    ShopSubProductEntity saved = shopSubProductRepository.save(toSaveShopSubProduct);
                    idToPriceMap.put(saved.getId(), toSaveShopSubProduct.getSellingPrice());
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
                                toSaveShopSubProduct.setSellingPrice(map.get(Mrp).getSellingPrice());
                                toSaveShopSubProduct.setQuantity(map.get(Mrp).getQuantity());
                                ShopSubProductEntity saved = shopSubProductRepository.save(toSaveShopSubProduct);
                                idToPriceMap.put(saved.getId(), toSaveShopSubProduct.getSellingPrice());
                            }
                        });
            }
            responseDTO.setIdToPriceMap(idToPriceMap);
            return responseDTO;
        } else {
            throw new Exception(shopExistsResponse.getError().getMessage());
        }
    }

    public ShopSubProductDTO getShopSubProductById(HttpServletRequest request, RequestDTO requestDTO) throws Exception {
        shopExistenceCheck(requestDTO.getShopId());
        ShopSubProductEntity shopSubProduct = shopSubProductRepository.findById(requestDTO.getShopSubProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop sub-product with id: " + requestDTO.getShopSubProductId() + " not found."));
        if(!Objects.equals(shopSubProduct.getShopId(), requestDTO.getShopId())) {
            throw new Exception("Requested shop sub-product is not owned by your shop.");
        }
        ShopSubProductDTO shopSubProductDTO = modelMapper.map(shopSubProduct, ShopSubProductDTO.class);
        shopSubProductDTO.setMrp(shopSubProduct.getSubProduct().getMrp());
        return shopSubProductDTO;
    }

    public MessageDTO removeShopSubProductById(HttpServletRequest request, RequestDTO requestDTO) throws Exception {
        shopExistenceCheck(requestDTO.getShopId());
        ShopSubProductEntity shopSubProduct = shopSubProductRepository.findById(requestDTO.getShopSubProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop sub-product with id: " + requestDTO.getShopSubProductId() + " not found."));
        if(shopSubProduct.getShopId().equals(requestDTO.getShopId())) {
            throw new Exception("Requested shop sub-product is not owned by your shop.");
        }
        shopSubProductRepository.deleteById(requestDTO.getShopSubProductId());
        return new MessageDTO("Shop sub-product removed successfully");
    }

    public ShopSubProductDTO getShopSubProductByIds(HttpServletRequest request, Long subProductId, Long shopId) {
        ShopSubProductEntity shopSubProduct = shopSubProductRepository.findByIdAndShopId(subProductId, shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop sub-product not found with sub-product id: " + subProductId + " and shop id: " + shopId));
        return modelMapper.map(shopSubProduct, ShopSubProductDTO.class);
    }

    public MessageDTO updateStock(HttpServletRequest request, SubProductStockUpdateDTO updateDTO) {
        ShopSubProductEntity shopSubProduct = shopSubProductRepository.findByIdAndShopId(updateDTO.getSubProductId(), updateDTO.getShopId())
                .orElseThrow(() -> new ResourceNotFoundException("Sub-product not found."));
        if(updateDTO.getStockUpdate().equals(StockUpdate.INCREASE)) {
            shopSubProduct.setStock(shopSubProduct.getStock() + updateDTO.getStock());
        }
        else {
            if(shopSubProduct.getStock() < updateDTO.getStock()) {
                throw new RuntimeException("Insufficient stock.");
            }
            shopSubProduct.setStock(shopSubProduct.getStock() - updateDTO.getStock());
        }
        shopSubProductRepository.save(shopSubProduct);
        return new MessageDTO("Stock updated successfully.");
    }

    private void shopExistenceCheck(Long shopId) throws Exception {
        ApiResponse<DataDTO<Boolean>> shopExistsData = shopFeignClient.shopExistsById(shopId);
        if(shopExistsData.getData() == null) {
            throw new Exception(shopExistsData.getError().getMessage());
        }
        if(!shopExistsData.getData().getData()) {
            throw new ResourceNotFoundException("Shop with id: " + shopId + " not found.");
        }
    }

    public List<ShopProductDTO> getShopSubProductsByShopId(HttpServletRequest request, Long shopId) {
        List<ShopSubProductEntity> shopSubProductList = shopSubProductRepository.findByShopId(shopId);
        HashMap<ProductEntity, List<ShopSubProduct2DTO>> subProductHashMap = new HashMap<>();
        List<ShopProductDTO> shopSubProducts = new ArrayList<>();

        shopSubProductList.forEach(shopSubProduct -> {
            ProductEntity key = shopSubProduct.getSubProduct().getProduct();
            ShopSubProduct2DTO temp = new ShopSubProduct2DTO();
            temp.setId(shopSubProduct.getId());
            temp.setMrp(shopSubProduct.getSubProduct().getMrp());
            temp.setSellingPrice(shopSubProduct.getSellingPrice());
            temp.setStock(shopSubProduct.getStock());

            if(subProductHashMap.containsKey(key)) {
                subProductHashMap.get(key).add(temp);
            }
            else {
                ArrayList<ShopSubProduct2DTO> list = new ArrayList<>();
                subProductHashMap.put(key, list);
                subProductHashMap.get(key).add(temp);
            }
        });

        Set<ProductEntity> productSet = subProductHashMap.keySet();
        for(ProductEntity product : productSet) {
            List<ShopSubProduct2DTO> shopSubProductEntityList = subProductHashMap.get(product);
            ShopProductDTO shopProductDTO = modelMapper.map(product, ShopProductDTO.class);
            shopProductDTO.setShopSubProducts(shopSubProductEntityList);
            shopSubProducts.add(shopProductDTO);
        }
        return shopSubProducts;
    }

}
