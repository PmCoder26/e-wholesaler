package com.parimal.e_wholesaler.product_service.services;

import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitDTO;
import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitRequestDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.AddSubProductsForShopRequestDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.AddSubProductsForShopResponseDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.SubProductDTO2;
import com.parimal.e_wholesaler.common.dtos.sub_product.SubProductRequestDTO;
import com.parimal.e_wholesaler.common.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.product_service.dtos.shop_sub_product.ShopSubProductDTO;
import com.parimal.e_wholesaler.product_service.entities.ProductEntity;
import com.parimal.e_wholesaler.product_service.entities.ShopSellingUnitEntity;
import com.parimal.e_wholesaler.product_service.entities.ShopSubProductEntity;
import com.parimal.e_wholesaler.product_service.entities.SubProductEntity;
import com.parimal.e_wholesaler.product_service.repositories.ProductRepository;
import com.parimal.e_wholesaler.product_service.repositories.SellingUnitRepository;
import com.parimal.e_wholesaler.product_service.repositories.ShopSubProductRepository;
import com.parimal.e_wholesaler.product_service.repositories.SubProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ShopSubProductService {

    private final ProductRepository productRepository;
    private final SubProductRepository subProductRepository;
    private final ShopSubProductRepository shopSubProductRepository;

    private final SellingUnitRepository sellingUnitRepository;
    private final ModelMapper modelMapper;


    @Transactional
    public AddSubProductsForShopResponseDTO addSubProductsForShop(Long shopId, Long productId, AddSubProductsForShopRequestDTO req) {

        AddSubProductsForShopResponseDTO response = new AddSubProductsForShopResponseDTO();
        List<SubProductDTO2> addedSubProducts = new ArrayList<>();
        boolean isAnythingAdded = false;

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        for (SubProductRequestDTO spReq : req.getSubProducts()) {

            // 1️⃣ SUB-PRODUCT
            SubProductEntity subProduct = subProductRepository
                    .findByProduct_IdAndMrp(product.getId(), spReq.getMrp())
                    .orElseGet(() -> {
                        SubProductEntity sp = new SubProductEntity();
                        sp.setProduct(product);
                        sp.setMrp(spReq.getMrp());
                        return subProductRepository.save(sp);
                    });

            // 2️⃣ SHOP-SUB-PRODUCT
            ShopSubProductEntity shopSubProduct = shopSubProductRepository
                    .findByShopIdAndSubProduct_Id(shopId, subProduct.getId())
                    .orElseGet(() -> {
                        ShopSubProductEntity ssp = new ShopSubProductEntity();
                        ssp.setShopId(shopId);
                        ssp.setSubProduct(subProduct);
                        return shopSubProductRepository.save(ssp);
                    });

            List<SellingUnitDTO> addedUnits = new ArrayList<>();

            // 3️⃣ SELLING UNITS
            if (spReq.getSellingUnits() != null) {
                for (SellingUnitRequestDTO unitReq : spReq.getSellingUnits()) {

                    boolean exists = sellingUnitRepository
                            .existsByShopSubProduct_IdAndUnitType(
                                    shopSubProduct.getId(),
                                    unitReq.getUnitType()
                            );

                    if (exists) continue;

                    ShopSellingUnitEntity unit = new ShopSellingUnitEntity();
                    unit.setShopSubProduct(shopSubProduct);
                    unit.setUnitType(unitReq.getUnitType());
                    unit.setPackets(unitReq.getPackets());
                    unit.setSellingPrice(unitReq.getSellingPrice());
                    unit = sellingUnitRepository.save(unit);

                    SellingUnitDTO unitDTO = new SellingUnitDTO();
                    unitDTO.setId(unit.getId());
                    unitDTO.setUnitType(unit.getUnitType());
                    unitDTO.setPackets(unit.getPackets());
                    unitDTO.setSellingPrice(unit.getSellingPrice());

                    addedUnits.add(unitDTO);
                    isAnythingAdded = true;
                }
            }

            // 4️⃣ Add sub-product ONLY if something was added
            if (!addedUnits.isEmpty()) {
                SubProductDTO2 subDTO = new SubProductDTO2();
                subDTO.setId(shopSubProduct.getId());
                subDTO.setMrp(subProduct.getMrp());
                subDTO.setSellingUnits(addedUnits);

                addedSubProducts.add(subDTO);
            }
        }

        if (isAnythingAdded) {
            response.setMessage("New product details were added, and existing ones were skipped");
            response.setAddedSubProducts(addedSubProducts);
        } else {
            response.setMessage("All provided sub-product details already exist for this shop");
        }

        return response;
    }

    public ShopSubProductDTO getShopSubProductByIds(Long subProductId, Long shopId) {
        ShopSubProductEntity shopSubProduct = shopSubProductRepository.findByIdAndShopId(subProductId, shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop sub-product not found with sub-product id: " + subProductId + " and shop id: " + shopId));
        return modelMapper.map(shopSubProduct, ShopSubProductDTO.class);
    }

    public List<SubProductDTO2> getShopProductDetails(Long shopId, Long productId) {

        List<ShopSubProductEntity> shopSubProducts = shopSubProductRepository.findByShopIdAndProductId(shopId, productId);

        return shopSubProducts.stream()
                .map( ssp -> {
                    List<SellingUnitDTO> sellingUnits = ssp.getSellingUnits()
                            .stream()
                            .map(su -> modelMapper.map(su, SellingUnitDTO.class))
                            .toList();
                    return new SubProductDTO2(ssp.getId(), ssp.getSubProduct().getMrp(), sellingUnits);
                }).toList();
    }

    @Transactional
    public void deleteShopSubProduct(Long shopId, Long shopSubProductId) {

        ShopSubProductEntity shopSubProduct = shopSubProductRepository.findByIdAndShopId(shopSubProductId, shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Sub-Product not found"));

        shopSubProductRepository.delete(shopSubProduct);
    }

}
