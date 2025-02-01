package com.parimal.e_wholesaler.shop_service.controllers;

import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerDTO;
import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerHomeDTO;
import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerResponseDTO;
import com.parimal.e_wholesaler.shop_service.services.OwnerService;
import jakarta.servlet.http.HttpServletRequest;
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
            HttpServletRequest request,
            @RequestBody @Valid
            OwnerRequestDTO requestDTO
    ) {
        return ownerService.createOwner(request, requestDTO);
    }

    @GetMapping(path = "/{id}")
    public OwnerDTO getOwnerById(
            HttpServletRequest request,
            @PathVariable
            Long id
    ) {
        return ownerService.getOwnerById(request, id);
    }

    @GetMapping(path = "/home/{ownerId}")
    public OwnerHomeDTO getHomeDetails(
            HttpServletRequest request,
            @PathVariable
            Long ownerId
    ) {
        return ownerService.getHomeDetails(request, ownerId);
    }

    @DeleteMapping(path = "/{id}")
    public MessageDTO deleteOwnerById(
            HttpServletRequest request,
            @PathVariable
            Long id
    ) {
        return ownerService.deleteOwnerById(request, id);
    }



}
