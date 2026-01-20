package com.parimal.e_wholesaler.shop_service.services.owner;

import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopEditRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopResponseDTO;
import com.parimal.e_wholesaler.shop_service.entities.OwnerEntity;
import com.parimal.e_wholesaler.shop_service.entities.ShopEntity;
import com.parimal.e_wholesaler.common.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.common.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.common.exceptions.UnAuthorizedAccessException;
import com.parimal.e_wholesaler.shop_service.repositories.OwnerRepository;
import com.parimal.e_wholesaler.shop_service.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OwnerShopService {

    private final OwnerRepository ownerRepository;
    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;


    public List<ShopDTO> getShopsByOwnerId(Long ownerId) {
        OwnerEntity owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner with id: " + ownerId + " not found."));
        return owner.getShops()
                .stream()
                .map(shop -> modelMapper.map(shop, ShopDTO.class))
                .toList();
    }

    public ShopResponseDTO createShop(Long ownerId, ShopRequestDTO requestDTO) {
        boolean shopExists = shopRepository.existsByNameAndGstNo(requestDTO.getName(), requestDTO.getGstNo());
        if(shopExists) {
            throw new ResourceAlreadyExistsException("Shop already exists.");
        }
        OwnerEntity owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found."));
        ShopEntity toSave = modelMapper.map(requestDTO, ShopEntity.class);
        toSave.setOwner(owner);
        ShopEntity saved = shopRepository.save(toSave);
        return modelMapper.map(saved, ShopResponseDTO.class);
    }

    public ShopDTO updateShop(Long ownerId, ShopEditRequestDTO requestDTO) {
        ShopEntity shop = shopRepository.findById(requestDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id: " + requestDTO.getId() + " not found."));
        if(!shop.getOwner().getId().equals(ownerId))
            throw new UnAuthorizedAccessException(
                    "The shop with id " + ownerId + " doesn't belongs to you."
            );
        shop.setName(requestDTO.getName());
        shop.setGstNo(requestDTO.getGstNo());
        shop.setAddress(requestDTO.getAddress());
        shop.setCity(requestDTO.getCity());
        shop.setState(requestDTO.getState());
        ShopEntity saved = shopRepository.save(shop);
        return modelMapper.map(saved, ShopDTO.class);
    }

}
