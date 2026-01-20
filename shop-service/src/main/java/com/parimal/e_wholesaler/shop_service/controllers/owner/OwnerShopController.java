package com.parimal.e_wholesaler.shop_service.controllers.owner;

import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopEditRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopRequestDTO;
import com.parimal.e_wholesaler.shop_service.dtos.shop.ShopResponseDTO;
import com.parimal.e_wholesaler.shop_service.services.owner.OwnerShopService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/owner")
@PreAuthorize(value = "hasRole('OWNER')")
@AllArgsConstructor
public class OwnerShopController {

    private final OwnerShopService ownerShopService;
    

    @GetMapping(path = "/{ownerId}/shops")
    public List<ShopDTO> getShopsByOwnerId(
            @PathVariable
            Long ownerId
    ) {
        return ownerShopService.getShopsByOwnerId(ownerId);
    }

    @PostMapping(path = "/{ownerId}/shop")
    public ShopResponseDTO createShop(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ShopRequestDTO requestDTO
    ) {
        return ownerShopService.createShop(ownerId, requestDTO);
    }

    @PutMapping(path = "/{ownerId}/shop")
    public ShopDTO updateShop(
            HttpServletRequest request,
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ShopEditRequestDTO requestDTO
    ) {
        return ownerShopService.updateShop(ownerId, requestDTO);
    }

}
