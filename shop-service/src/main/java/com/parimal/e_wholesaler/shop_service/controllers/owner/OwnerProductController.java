package com.parimal.e_wholesaler.shop_service.controllers.owner;

import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopRequestDTO;
import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopResponseDTO;
import com.parimal.e_wholesaler.common.dtos.product.ProductIdentityDTO;
import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitDTO;
import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitRequestDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.AddSubProductsForShopRequestDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.AddSubProductsForShopResponseDTO;
import com.parimal.e_wholesaler.common.dtos.sub_product.SubProductDTO2;
import com.parimal.e_wholesaler.shop_service.services.owner.OwnerProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/owner")
@PreAuthorize(value = "hasRole('OWNER')")
@AllArgsConstructor
public class OwnerProductController {

    private final OwnerProductService ownerProductService;
    

    @GetMapping(path = "/{ownerId}/shop/{shopId}/products")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public List<ProductIdentityDTO> getShopProducts(
            @PathVariable
            Long ownerId,
            @PathVariable
            Long shopId
    ) {
        return ownerProductService.getShopProducts(ownerId, shopId);
    }

    @PostMapping(path = "/{ownerId}/shop/{shopId}/products")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public AddProductForShopResponseDTO addProduct(
            @PathVariable
            Long ownerId,
            @PathVariable
            Long shopId,
            @RequestBody @Valid
            AddProductForShopRequestDTO requestDTO
    ) {
        return ownerProductService.addProduct(ownerId, shopId, requestDTO);
    }

    @DeleteMapping(path = "/{ownerId}/shop/{shopId}/products/{productId}")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteShopProduct(
            @PathVariable
            Long ownerId,
            @PathVariable
            Long shopId,
            @PathVariable
            Long productId
    ) {
        ownerProductService.deleteShopProduct(ownerId, shopId, productId);
    }

    @PostMapping(path = "/{ownerId}/shop/{shopId}/products/{productId}/sub-products")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public AddSubProductsForShopResponseDTO addShopSubProduct(
            @PathVariable Long ownerId,
            @PathVariable Long shopId,
            @PathVariable Long productId,
            @RequestBody @Valid
            AddSubProductsForShopRequestDTO requestDTO
    ) {
        return ownerProductService.addShopSubProduct(ownerId, shopId, productId,requestDTO);
    }

    @GetMapping(path = "/{ownerId}/shop/{shopId}/products/{productId}/sub-products")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public List<SubProductDTO2> getShopProductDetails(
            @PathVariable Long ownerId,
            @PathVariable Long shopId,
            @PathVariable Long productId
    ) {
        return ownerProductService.getShopProductDetails(ownerId, shopId, productId);
    }

    @DeleteMapping(path = "/{ownerId}/shop/{shopId}/sub-products/{shopSubProductId}")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteShopSubProduct(
            @PathVariable Long ownerId,
            @PathVariable Long shopId,
            @PathVariable Long shopSubProductId
    ) {
        ownerProductService.deleteShopSubProduct(ownerId, shopId, shopSubProductId);
    }

    @PostMapping(path = "/{ownerId}/shop/{shopId}/sub-products/{shopSubProductId}/selling-units")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public SellingUnitDTO addProductSellingUnit(
            @PathVariable Long ownerId,
            @PathVariable Long shopId,
            @PathVariable Long shopSubProductId,
            @RequestBody @Valid SellingUnitRequestDTO requestDTO
    ) {
        return ownerProductService.addProductSellingUnit(ownerId, shopId, shopSubProductId, requestDTO);
    }

    @PutMapping(path = "/{ownerId}/shop/{shopId}/sub-products/{shopSubProductId}/selling-units/{sellingUnitId}")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    public SellingUnitDTO updateProductSellingUnit(
            @PathVariable Long ownerId,
            @PathVariable Long shopId,
            @PathVariable Long shopSubProductId,
            @PathVariable Long sellingUnitId,
            @RequestBody @Valid SellingUnitRequestDTO requestDTO
            ) {
        return ownerProductService.updateProductSellingUnit(ownerId, shopId, shopSubProductId, sellingUnitId, requestDTO);
    }

    @DeleteMapping(path = "/{ownerId}/shop/{shopId}/sub-products/{shopSubProductId}/selling-units/{sellingUnitId}")
    @PreAuthorize(value = "@ownerSecurity.isAuthorizedOwner(#ownerId)")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductSellingUnit(
            @PathVariable Long ownerId,
            @PathVariable Long shopId,
            @PathVariable Long shopSubProductId,
            @PathVariable Long sellingUnitId
    ) {
        ownerProductService.deleteProductSellingUnit(ownerId, shopId, shopSubProductId, sellingUnitId);
    }

    
}
