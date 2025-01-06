package com.parimal.e_wholesaler.shop_service.services;

import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.OwnerDTO;
import com.parimal.e_wholesaler.shop_service.dtos.OwnerRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.OwnerResponseDTO;
import com.parimal.e_wholesaler.shop_service.entities.OwnerEntity;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.shop_service.repositories.OwnerRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final ModelMapper modelMapper;

    public OwnerResponseDTO createOwner(OwnerRequestDTO requestDTO) {
         boolean ownerExists = ownerRepository.existsByMobNo(requestDTO.getMobNo());
         if(!ownerExists) {
             OwnerEntity toSave = modelMapper.map(requestDTO, OwnerEntity.class);
             OwnerEntity saved = ownerRepository.save(toSave);
             return modelMapper.map(saved, OwnerResponseDTO.class);
         }
         throw new ResourceAlreadyExistsException("Owner already exists or use another mobile number");
    }

    public OwnerDTO getOwnerById(Long id) {
        OwnerEntity owner = ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner with id: " + id + " not found."));
        return modelMapper.map(owner, OwnerDTO.class);
    }

    public MessageDTO deleteOwnerById(Long id) {
        boolean ownerExists = ownerRepository.existsById(id);
        if(ownerExists) {
            ownerRepository.deleteById(id);
            return new MessageDTO("Customer deleted successfully.");
        }
        throw new ResourceNotFoundException("Owner with id: " + id + " not found.");
    }

}
