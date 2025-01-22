package com.parimal.e_wholesaler.product_service.controllers;

import com.parimal.e_wholesaler.product_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.product_service.dtos.sub_product.SubProductDTO;
import com.parimal.e_wholesaler.product_service.dtos.sub_product.SubProductRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.sub_product.SubProductResponseDTO;
import com.parimal.e_wholesaler.product_service.services.SubProductService;
import jakarta.servlet.http.HttpServletRequest;
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
            HttpServletRequest request,
            @RequestBody @Valid SubProductRequestDTO requestDTO
    ) {
        return subProductService.addSubProduct(request, requestDTO);
    }

    @GetMapping(path = "/{id}")
    public SubProductDTO getSubProductById(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        return subProductService.getSubProductById(request, id);
    }

    @DeleteMapping(path = "/{id}")
    public MessageDTO removeSubProductById(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        return subProductService.removeSubProductById(request, id);
    }

}
