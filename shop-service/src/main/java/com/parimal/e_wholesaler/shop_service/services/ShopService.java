package com.parimal.e_wholesaler.shop_service.services;

import com.parimal.e_wholesaler.shop_service.advices.ApiResponse;
import com.parimal.e_wholesaler.shop_service.clients.ProductFeignClient;
import com.parimal.e_wholesaler.shop_service.dtos.*;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopEditRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopResponseDTO;
import com.parimal.e_wholesaler.shop_service.entities.OwnerEntity;
import com.parimal.e_wholesaler.shop_service.entities.ShopEntity;
import com.parimal.e_wholesaler.shop_service.exceptions.MyException;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.shop_service.exceptions.UnAuthorizedAccessException;
import com.parimal.e_wholesaler.shop_service.repositories.OwnerRepository;
import com.parimal.e_wholesaler.shop_service.repositories.ShopRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ShopService {

    private final ShopRepository shopRepository;
    private final OwnerService ownerService;

    private final ModelMapper modelMapper;

    private final ProductFeignClient productFeignClient;


    public ShopResponseDTO createShop(HttpServletRequest request, ShopRequestDTO requestDTO) {
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

    public ShopDTO getShopById(HttpServletRequest request, Long id) {
        ShopEntity shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id: " + id + " not found."));
        return modelMapper.map(shop, ShopDTO.class);
    }

    public MessageDTO deleteShopById(HttpServletRequest request, Long id) {
        boolean exists = shopRepository.existsById(id);
        if(!exists) {
            throw new ResourceNotFoundException("Shop with id: " + id + " not found.");
        }
        shopRepository.deleteById(id);
        return new MessageDTO("Shop deleted successfully.");
    }

    public DataDTO<Boolean> existsById(HttpServletRequest request, Long id) {
        boolean shopExists = shopRepository.existsById(id);
        return new DataDTO<>(shopExists);
    }

    public ShopDTO updateShop(HttpServletRequest request, ShopEditRequestDTO requestDTO) {
        ShopEntity shop = shopRepository.findById(requestDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id: " + requestDTO.getId() + " not found."));
        if(!shop.getOwner().getId().equals(requestDTO.getOwnerId()))
            throw new UnAuthorizedAccessException(
                    "The shop with id " + requestDTO.getOwnerId() + " doesn't belongs to you."
            );
        shop.setName(requestDTO.getName());
        shop.setGstNo(requestDTO.getGstNo());
        shop.setAddress(requestDTO.getAddress());
        shop.setCity(requestDTO.getCity());
        shop.setState(requestDTO.getState());
        ShopEntity saved = shopRepository.save(shop);
        return modelMapper.map(saved, ShopDTO.class);
    }

    public List<ShopProductDTO> getProductsByShopId(HttpServletRequest request, Long shopId) {
        boolean shopExists = shopRepository.existsById(shopId);
        if(!shopExists) {
            throw new ResourceNotFoundException("Shop with id: " + shopId + " now found.");
        }
        ApiResponse<List<ShopProductDTO>> productsResponse = productFeignClient.getShopSubProductsByShopId(shopId);
        if(productsResponse.getError() != null) {
            throw new MyException(productsResponse.getError());
        }
        return productsResponse.getData();
    }

}
