package com.parimal.e_wholesaler.shop_service.services;

import com.parimal.e_wholesaler.shop_service.dtos.*;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopResponseDTO;
import com.parimal.e_wholesaler.shop_service.entities.OwnerEntity;
import com.parimal.e_wholesaler.shop_service.entities.ShopEntity;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.shop_service.repositories.OwnerRepository;
import com.parimal.e_wholesaler.shop_service.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class ShopService {

    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;
    private final OwnerService ownerService;


    public ShopResponseDTO createShop(ShopRequestDTO requestDTO) {
         boolean shopExists = shopRepository.existsByNameAndGstNo(requestDTO.getName(), requestDTO.getGstNo());
         if(shopExists) {
             throw new ResourceAlreadyExistsException("Shop already exists.");
         }
        OwnerEntity owner = ownerService.getOwnerEntityById(requestDTO.getOwnerId());
        ShopEntity toSave = modelMapper.map(requestDTO, ShopEntity.class);
        toSave.setOwner(owner);
        ShopEntity saved = shopRepository.save(toSave);
        return modelMapper.map(saved, ShopResponseDTO.class);
    }

    public ShopDTO getShopById(Long id) {
        ShopEntity shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id: " + id + " not found."));
        return modelMapper.map(shop, ShopDTO.class);
    }

    public MessageDTO deleteShopById(Long id) {
        boolean exists = shopRepository.existsById(id);
        if(!exists) {
            throw new ResourceNotFoundException("Shop with id: " + id + " not found.");
        }
        shopRepository.deleteById(id);
        return new MessageDTO("Shop deleted successfully.");
    }

    public DataDTO<Boolean> existsById(Long id) {
        boolean shopExists = shopRepository.existsById(id);
        return new DataDTO<>(shopExists);
    }
}
