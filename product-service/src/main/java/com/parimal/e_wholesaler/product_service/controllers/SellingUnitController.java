package com.parimal.e_wholesaler.product_service.controllers;

import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitDTO;
import com.parimal.e_wholesaler.common.dtos.shop_selling_unit.SellingUnitRequestDTO;
import com.parimal.e_wholesaler.product_service.services.SellingUnitService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/internal/shops")
@AllArgsConstructor
public class SellingUnitController {

    private final SellingUnitService sellingUnitService;


    @PostMapping(path = "/{shopId}/sub-products/{shopSubProductId}/selling-units")
    @PreAuthorize(value = "hasAuthority('PRODUCT_CREATE')")
    public SellingUnitDTO addSellingUnit(
            @PathVariable Long shopId,
            @PathVariable Long shopSubProductId,
            @RequestBody @Valid SellingUnitRequestDTO requestDTO
    ) {
        return sellingUnitService.addSellingUnit(shopId, shopSubProductId, requestDTO);
    }

    @PutMapping(path = "/{shopId}/sub-products/{shopSubProductId}/selling-units/{sellingUnitId}")
    @PreAuthorize(value = "hasAuthority('SHOP_PRODUCT_UPDATE')")
    public SellingUnitDTO updateProductSellingUnit(
            @PathVariable Long shopId,
            @PathVariable Long shopSubProductId,
            @PathVariable Long sellingUnitId,
            @RequestBody @Valid SellingUnitRequestDTO requestDTO
    ) {
        return sellingUnitService.updateProductSellingUnit(shopId, shopSubProductId, sellingUnitId, requestDTO);
    }

    @DeleteMapping(path = "/{shopId}/sub-products/{shopSubProductId}/selling-units/{sellingUnitId}")
    @PreAuthorize(value = "hasAuthority('SHOP_PRODUCT_DELETE')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductSellingUnit(
            @PathVariable Long shopId,
            @PathVariable Long shopSubProductId,
            @PathVariable Long sellingUnitId
    ) {
        sellingUnitService.deleteProductSellingUnit(shopId, shopSubProductId, sellingUnitId);
    }

}
