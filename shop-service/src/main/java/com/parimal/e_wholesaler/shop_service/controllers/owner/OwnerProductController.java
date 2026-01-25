package com.parimal.e_wholesaler.shop_service.controllers.owner;

import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopRequestDTO;
import com.parimal.e_wholesaler.common.dtos.product.AddProductForShopResponseDTO;
import com.parimal.e_wholesaler.common.dtos.product.ProductIdentityDTO;
import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitDTO;
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
    public List<ProductIdentityDTO> getShopProducts(
            @PathVariable
            Long ownerId,
            @PathVariable
            Long shopId
    ) {
        return ownerProductService.getShopProducts(ownerId, shopId);
    }

    @PostMapping(path = "/{ownerId}/shop/{shopId}/products")
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

    @PostMapping(path = "/{ownerId}/shop/{shopId}/products/{productId}/sub-products")
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
    public List<SubProductDTO2> getShopProductDetails(
            @PathVariable Long ownerId,
            @PathVariable Long shopId,
            @PathVariable Long productId
    ) {
        return ownerProductService.getShopProductDetails(ownerId, shopId, productId);
    }

    @DeleteMapping(path = "/{ownerId}/shop/{shopId}/sub-products/{shopSubProductId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteShopSubProduct(
            @PathVariable Long ownerId,
            @PathVariable Long shopId,
            @PathVariable Long shopSubProductId
    ) {
        ownerProductService.deleteShopSubProduct(ownerId, shopId, shopSubProductId);
    }

    @PutMapping(path = "/{ownerId}/shop/{shopId}/sub-products/{shopSubProductId}/selling-units/{sellingUnitId}")
    public SellingUnitDTO updateProductSellingUnit(
            @PathVariable Long ownerId,
            @PathVariable Long shopId,
            @PathVariable Long shopSubProductId,
            @PathVariable Long sellingUnitId,
            @RequestBody Map<String, Object> updates
    ) {
        return ownerProductService.updateProductSellingUnit(ownerId, shopId, shopSubProductId, sellingUnitId, updates);
    }

    @DeleteMapping(path = "/{ownerId}/shop/{shopId}/sub-products/{shopSubProductId}/selling-units/{sellingUnitId}")
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
