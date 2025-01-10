package com.parimal.e_wholesaler.shop_service.controllers;

import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerDTO;
import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerResponseDTO;
import com.parimal.e_wholesaler.shop_service.services.OwnerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/owner")
@AllArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;


    @PostMapping
    public OwnerResponseDTO createOwner(
            @RequestBody @Valid
            OwnerRequestDTO requestDTO
    ) {
        return ownerService.createOwner(requestDTO);
    }

    @GetMapping(path = "/{id}")
    public OwnerDTO getOwnerById(
            @PathVariable
            Long id
    ) {
        return ownerService.getOwnerById(id);
    }

    @DeleteMapping(path = "/{id}")
    public MessageDTO deleteOwnerById(
            @PathVariable
            Long id
    ) {
        return ownerService.deleteOwnerById(id);
    }

}
