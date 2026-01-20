package com.parimal.e_wholesaler.shop_service.controllers.owner;

import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.owner.OwnerResponseDTO;
import com.parimal.e_wholesaler.shop_service.services.owner.OwnerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner/auth")
@PreAuthorize(value = "hasRole('USER_SERVICE')")
@AllArgsConstructor
public class OwnerAuthController {

    private final OwnerService ownerService;


    @PostMapping
    public OwnerResponseDTO createOwner(
            @RequestBody @Valid
            OwnerRequestDTO requestDTO
    ) {
        return ownerService.createOwner(requestDTO);
    }

    @PostMapping(path = "/id")
    public Long getOwnerIdByMobNo(
            @RequestBody String mobNo
    ) {
        return ownerService.getOwnerIdByMobNo(mobNo);
    }
}
