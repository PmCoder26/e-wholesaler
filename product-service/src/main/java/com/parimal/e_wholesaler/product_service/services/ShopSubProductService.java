package com.parimal.e_wholesaler.product_service.services;

import com.parimal.e_wholesaler.common.advices.ApiResponse;
import com.parimal.e_wholesaler.product_service.clients.ShopFeignClient;
import com.parimal.e_wholesaler.product_service.dtos.*;
import com.parimal.e_wholesaler.product_service.dtos.product.ProductRemoveRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.*;
import com.parimal.e_wholesaler.product_service.entities.ProductEntity;
import com.parimal.e_wholesaler.product_service.entities.ShopSubProductEntity;
import com.parimal.e_wholesaler.product_service.entities.SubProductEntity;
import com.parimal.e_wholesaler.common.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.common.exceptions.ResourceNotFoundException;
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
            Optional<ProductEntity> product = productService.findByNameIgnoreCase(request, requestDTO.getProductName());
            ShopSubProductResponseDTO responseDTO = new ShopSubProductResponseDTO();
            HashMap<Long, Double> idToPriceMap = new HashMap<>();
            HashMap<Double, QuantityToSellingPrice> map = requestDTO.getMrpToSelling();
            Set<Double> MRPs = map.keySet();
            if (product.isEmpty()) {
                ProductEntity toSaveProduct = modelMapper.map(requestDTO, ProductEntity.class);
                ProductEntity savedProduct = productService.saveProduct(request, toSaveProduct);
                responseDTO.setProductId(savedProduct.getId());
                for (Double Mrp : MRPs) {
                    SubProductEntity savedSubProduct = subProductService.addSubProduct(savedProduct, Mrp);
                    ShopSubProductEntity toSaveShopSubProduct = new ShopSubProductEntity();
                    toSaveShopSubProduct.setSubProduct(savedSubProduct);
                    toSaveShopSubProduct.setShopId(requestDTO.getShopId());
                    QuantityToSellingPrice value = map.get(Mrp);
                    toSaveShopSubProduct.setSellingPrice(value.getSellingPrice());
                    toSaveShopSubProduct.setQuantity(value.getQuantity());
                    toSaveShopSubProduct.setStock(value.getStock());
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
                                QuantityToSellingPrice value = map.get(Mrp);
                                toSaveShopSubProduct.setSellingPrice(value.getSellingPrice());
                                toSaveShopSubProduct.setQuantity(value.getQuantity());
                                toSaveShopSubProduct.setStock(value.getStock());
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
        ShopSubProductEntity shopSubProduct = shopSubProductRepository.findById(requestDTO.getShopSubProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop sub-product with id: " + requestDTO.getShopSubProductId() + " not found."));
        if(!shopSubProduct.getShopId().equals(requestDTO.getShopId())) {
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
            temp.setQuantity(shopSubProduct.getQuantity());
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

    @Transactional  // no need to use .save() when @Transactional used.
    public MessageDTO updateShopSubProduct(HttpServletRequest request, ShopSubProductUpdateRequestDTO requestDTO) {
        ShopSubProductEntity shopSubProduct = shopSubProductRepository.findByIdAndShopId(requestDTO.getId(), requestDTO.getShopId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop's sub-product not found."));

        if(areEntityAndRequestDTOSame(shopSubProduct, requestDTO))
            throw new ResourceAlreadyExistsException("Data provided is same as of old shop's sub-product.");

        shopSubProduct.setSellingPrice(requestDTO.getSellingPrice());
        shopSubProduct.setQuantity(requestDTO.getQuantity());
        shopSubProduct.setStock(requestDTO.getStock());
        shopSubProduct.getSubProduct().setMrp(requestDTO.getMrp());

        return new MessageDTO("Shop's sub-product updated successfully");
    }

    @Transactional
    public MessageDTO removeProductByShopIdAndProductName(HttpServletRequest request, ProductRemoveRequestDTO requestDTO) {
        boolean productExists = productService.existsByName(request, requestDTO.getProductName());
        if(!productExists) throw new ResourceNotFoundException("Product with name " + requestDTO.getProductName() + " not found.");

        long shopSubProductsDeleteCount = shopSubProductRepository.deleteAllByShopIdAndSubProduct_Product_Name(requestDTO.getShopId(), requestDTO.getProductName());

        MessageDTO message = new MessageDTO("Failed to delete the product or product doesn't exists");
        if(shopSubProductsDeleteCount > 0) message.setMessage("Product deleted successfully");

        return message;
    }

    private boolean areEntityAndRequestDTOSame(ShopSubProductEntity shopSubProduct, ShopSubProductUpdateRequestDTO requestDTO) {
        return shopSubProduct.getQuantity().equals(requestDTO.getQuantity())
                && shopSubProduct.getStock().equals(requestDTO.getStock())
                && shopSubProduct.getSellingPrice().equals(requestDTO.getSellingPrice())
                && shopSubProduct.getSubProduct().getMrp().equals(requestDTO.getMrp());
    }
}
