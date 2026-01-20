package com.parimal.e_wholesaler.shop_service.controllers.owner;

import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.dtos.product.*;
import com.parimal.e_wholesaler.shop_service.services.owner.OwnerProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/owner")
@PreAuthorize(value = "hasRole('OWNER')")
@AllArgsConstructor
public class OwnerProductController {

    private final OwnerProductService ownerProductService;
    

    @GetMapping(path = "/{ownerId}/shop/{shopId}/products")
    public List<ShopProductDTO> getProductsByShopId(
            @PathVariable
            Long ownerId,
            @PathVariable
            Long shopId
    ) {
        return ownerProductService.getProductsByShopId(ownerId, shopId);
    }

    @PostMapping(path = "/{ownerId}/shop/products/shop-sub-product")
    public ShopSubProductResponseDTO addShopSubProduct(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ShopSubProductRequestDTO requestDTO
    ) {
        return ownerProductService.addShopSubProduct(ownerId,requestDTO);
    }

    @DeleteMapping(path = "/{ownerId}/shop/products/shop-sub-product")
    public MessageDTO removeShopSubProduct(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            RequestDTO requestDTO
    ) {
        return ownerProductService.removeShopSubProduct(ownerId, requestDTO);
    }

    @PutMapping(path = "/{ownerId}/shop/products/shop-sub-product")
    public MessageDTO updateShopSubProduct(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ShopSubProductUpdateRequestDTO requestDTO
    ) {
        return ownerProductService.updateShopSubProduct(ownerId, requestDTO);
    }

    @DeleteMapping(path = "/{ownerId}/shop/products/product")
    public MessageDTO removeProductByShopIdAndProductName(
            @PathVariable
            Long ownerId,
            @RequestBody @Valid
            ProductRemoveRequestDTO requestDTO
    ) {
        return ownerProductService.removeProductByShopIdAndProductName(ownerId, requestDTO);
    }
    
}
