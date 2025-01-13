package com.parimal.e_wholesaler.product_service.controllers;

import com.parimal.e_wholesaler.product_service.dtos.sub_product.SubProductDTO;
import com.parimal.e_wholesaler.product_service.dtos.sub_product.SubProductRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.sub_product.SubProductResponseDTO;
import com.parimal.e_wholesaler.product_service.services.SubProductService;
import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/sub-product")
@AllArgsConstructor
public class SubProductController {

    private final SubProductService subProductService;


    @PostMapping
    public SubProductResponseDTO addSubProduct(
            @RequestBody @Valid
            SubProductRequestDTO requestDTO
    ) {
        return subProductService.addSubProduct(requestDTO);
    }

    @GetMapping(path = "/{id}")
    public SubProductDTO getSubProductById(
            @PathVariable
            Long id
    ) {
        return subProductService.getSubProductById(id);
    }

    @DeleteMapping(path = "/{id}")
    public MessageDTO removeSubProductById(
            @PathVariable
            Long id
    ) {
        return subProductService.removeSubProductById(id);
    }

}
