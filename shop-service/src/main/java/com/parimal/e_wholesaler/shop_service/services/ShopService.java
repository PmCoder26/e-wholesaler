package com.parimal.e_wholesaler.shop_service.services;

import com.parimal.e_wholesaler.shop_service.dtos.*;
import com.parimal.e_wholesaler.shop_service.entities.OwnerEntity;
import com.parimal.e_wholesaler.shop_service.entities.ShopEntity;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.shop_service.repositories.OwnerRepository;
import com.parimal.e_wholesaler.shop_service.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;
    private final OwnerRepository ownerRepository;


    public ShopResponseDTO createShop(ShopRequestDTO requestDTO) {
         boolean shopExists = shopRepository.existsByGstNo(requestDTO.getGstNo());
         if(!shopExists) {
             OwnerEntity owner = ownerRepository.findById(requestDTO.getOwnerId())
                     .orElseThrow(() -> new ResourceNotFoundException("Owner with id: " + requestDTO.getOwnerId() + " not found."));
             ShopEntity toSave = modelMapper.map(requestDTO, ShopEntity.class);
             toSave.setOwner(owner);
             ShopEntity saved = shopRepository.save(toSave);
             return modelMapper.map(saved, ShopResponseDTO.class);
         }
         throw new ResourceAlreadyExistsException("Shop already exists.");
    }

    public ShopDTO getShopById(Long id) {
        ShopEntity shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id: " + id + " not found."));
        return modelMapper.map(shop, ShopDTO.class);
    }

    public MessageDTO deleteShopById(Long id) {
        boolean exists = shopRepository.existsById(id);
        if(exists) {
            shopRepository.deleteById(id);
            return new MessageDTO("Shop deleted successfully.");
        }
        throw new ResourceNotFoundException("Shop with id: " + id + " not found.");
    }

}
