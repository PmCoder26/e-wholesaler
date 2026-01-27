package com.parimal.e_wholesaler.shop_service.controllers.owner;

import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerDTO;
import com.parimal.e_wholesaler.shop_service.services.owner.OwnerService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/owner")
@AllArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;


    @GetMapping(path = "/{id}")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#id)")
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
