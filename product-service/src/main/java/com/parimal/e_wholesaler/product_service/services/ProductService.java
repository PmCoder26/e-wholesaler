package com.parimal.e_wholesaler.product_service.services;

import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopRequestDTO;
import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopResponseDTO;
import com.parimal.e_wholesaler.common.dtos.product.ProductDTO2;
import com.parimal.e_wholesaler.common.dtos.product.ProductIdentityDTO;
import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitDTO;
import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitRequestDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.SubProductDTO2;
import com.parimal.e_wholesaler.common.dtos.sub_product.SubProductRequestDTO;
import com.parimal.e_wholesaler.common.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.product_service.entities.ProductEntity;
import com.parimal.e_wholesaler.product_service.entities.ShopSellingUnitEntity;
import com.parimal.e_wholesaler.product_service.entities.ShopSubProductEntity;
import com.parimal.e_wholesaler.product_service.entities.SubProductEntity;
import com.parimal.e_wholesaler.product_service.repositories.ProductRepository;
import com.parimal.e_wholesaler.product_service.repositories.SellingUnitRepository;
import com.parimal.e_wholesaler.product_service.repositories.ShopSubProductRepository;
import com.parimal.e_wholesaler.product_service.repositories.SubProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SubProductRepository subProductRepository;
    private final ShopSubProductRepository shopSubProductRepository;
    private final SellingUnitRepository sellingUnitRepository;


    @Transactional
    public AddProductForShopResponseDTO addProductForShop(Long shopId, AddProductForShopRequestDTO req) {

        AddProductForShopResponseDTO response = new AddProductForShopResponseDTO();
        ProductDTO2 productDTO = new ProductDTO2();
        boolean isAnythingAdded = false;

        // 1️⃣ PRODUCT
        ProductEntity product = productRepository
                .findByNameIgnoreCaseAndCompanyIgnoreCase(req.getProductName(), req.getCompany())
                .orElseGet(() -> {
                    ProductEntity p = new ProductEntity();
                    p.setName(req.getProductName());
                    p.setCompany(req.getCompany());
                    return productRepository.save(p);
                });

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCompany(product.getCompany());

        List<SubProductDTO2> subDTOs = new ArrayList<>();

        for (SubProductRequestDTO spReq : req.getSubProducts()) {

            // 2️⃣ SUB-PRODUCT
            SubProductEntity subProduct = subProductRepository
                    .findByProduct_IdAndMrp(product.getId(), spReq.getMrp())
                    .orElseGet(() -> {
                        SubProductEntity sp = new SubProductEntity();
                        sp.setProduct(product);
                        sp.setMrp(spReq.getMrp());
                        return subProductRepository.save(sp);
                    });

            // 3️⃣ SHOP-SUB-PRODUCT
            ShopSubProductEntity shopSubProduct = shopSubProductRepository
                    .findByShopIdAndSubProduct_Id(shopId, subProduct.getId())
                    .orElseGet(() -> {
                        ShopSubProductEntity ssp = new ShopSubProductEntity();
                        ssp.setShopId(shopId);
                        ssp.setSubProduct(subProduct);
                        return shopSubProductRepository.save(ssp);
                    });

            SubProductDTO2 subDTO = new SubProductDTO2();
            subDTO.setId(shopSubProduct.getId());
            subDTO.setMrp(subProduct.getMrp());

            List<SellingUnitDTO> sellingUnitDTOs = new ArrayList<>();

            for (SellingUnitRequestDTO unitReq : spReq.getSellingUnits()) {

                Optional<ShopSellingUnitEntity> existingUnit = sellingUnitRepository
                        .findByShopSubProduct_IdAndUnitType(shopSubProduct.getId(), unitReq.getUnitType());

                if (existingUnit.isPresent()) continue;

                // Create new selling unit
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
                sellingUnitDTOs.add(unitDTO);

                isAnythingAdded = true;
            }

            if(!sellingUnitDTOs.isEmpty()) {
                subDTO.setSellingUnits(sellingUnitDTOs);
                subDTOs.add(subDTO);
            }
        }

        if(isAnythingAdded) {
            response.setMessage("New product details were added, and existing ones were skipped");
            response.setProduct(productDTO);
            productDTO.setSubProducts(subDTOs);
        } else {
            response.setMessage("This product already exists with all the provided details");
        }

        return response;
    }

    public List<ProductIdentityDTO> getShopProductForOwner(Long shopId) {
        return productRepository.findShopProductsByShopId(shopId);
    }

    @Transactional
    public void deleteShopProduct(Long shopId, Long productId) {
        long deletedRows = shopSubProductRepository.deleteAllByShopIdAndSubProduct_Product_Id(shopId, productId);
        if(deletedRows == 0) throw new ResourceNotFoundException("No such product found to delete");
    }

}
