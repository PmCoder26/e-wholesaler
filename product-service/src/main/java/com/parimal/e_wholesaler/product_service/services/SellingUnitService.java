package com.parimal.e_wholesaler.product_service.services;

import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitDTO;
import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitRequestDTO;
import com.parimal.e_wholesaler.common.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.common.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.product_service.entities.ShopSellingUnitEntity;
import com.parimal.e_wholesaler.product_service.entities.ShopSubProductEntity;
import com.parimal.e_wholesaler.product_service.repositories.SellingUnitRepository;
import com.parimal.e_wholesaler.product_service.repositories.ShopSubProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SellingUnitService {

    private final SellingUnitRepository sellingUnitRepository;
    private final ShopSubProductRepository shopSubProductRepository;

    private final ModelMapper modelMapper;


    public SellingUnitDTO addSellingUnit(Long shopId, Long shopSubProductId, SellingUnitRequestDTO requestDTO) {
        ShopSubProductEntity shopSubProduct = shopSubProductRepository.findByIdAndShopId(shopSubProductId, shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Sub-Product ID"));
        boolean sellingUnitExists = sellingUnitRepository
                .existsByShopSubProduct_IdAndUnitType(shopSubProductId, requestDTO.getUnitType());
        if(sellingUnitExists) throw new ResourceAlreadyExistsException("Unit already exists with type: " + requestDTO.getUnitType());

        ShopSellingUnitEntity toSave = modelMapper.map(requestDTO, ShopSellingUnitEntity.class);
        toSave.setShopSubProduct(shopSubProduct);

        ShopSellingUnitEntity saved = sellingUnitRepository.save(toSave);

        return modelMapper.map(saved, SellingUnitDTO.class);
    }

    @Transactional
    public SellingUnitDTO updateProductSellingUnit(
            Long shopId, Long shopSubProductId, Long sellingUnitId, SellingUnitRequestDTO requestDTO)
    {
        ShopSellingUnitEntity sellingUnit = sellingUnitRepository
                .findByIdAndShopSubProduct_IdAndShopSubProduct_ShopId(sellingUnitId, shopSubProductId, shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Selling-unit not found, enter valid data"));

        sellingUnit.setUnitType(requestDTO.getUnitType());
        sellingUnit.setPackets(requestDTO.getPackets());
        sellingUnit.setSellingPrice(requestDTO.getSellingPrice());

        ShopSellingUnitEntity updatedUnit = sellingUnitRepository.save(sellingUnit);

        return modelMapper.map(updatedUnit, SellingUnitDTO.class);
    }

    @Transactional
    public void deleteProductSellingUnit(Long shopId, Long shopSubProductId, Long sellingUnitId) {

        ShopSellingUnitEntity sellingUnit = sellingUnitRepository
                .findByIdAndShopSubProduct_IdAndShopSubProduct_ShopId(sellingUnitId, shopSubProductId, shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Selling-unit not found, enter valid data"));

        sellingUnitRepository.delete(sellingUnit);
    }

}
