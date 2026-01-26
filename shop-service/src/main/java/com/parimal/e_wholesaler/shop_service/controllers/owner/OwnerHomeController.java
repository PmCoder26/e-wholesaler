package com.parimal.e_wholesaler.shop_service.controllers.owner;

import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerHomeDTO;
import com.parimal.e_wholesaler.shop_service.services.owner.OwnerHomeService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/owner")
@PreAuthorize(value = "hasRole('OWNER')")
@AllArgsConstructor
public class OwnerHomeController {

    private final OwnerHomeService ownerHomeService;


    @GetMapping(path = "/{ownerId}/home")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public OwnerHomeDTO getHomeDetails(
            @PathVariable
            Long ownerId
    ) {
        return ownerHomeService.getHomeDetails(ownerId);
    }

}
