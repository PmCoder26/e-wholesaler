package com.parimal.e_wholesaler.product_service.controllers;

import com.parimal.e_wholesaler.product_service.dtos.product.ProductDTO;
import com.parimal.e_wholesaler.product_service.dtos.product.ProductRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.product.ProductResponseDTO;
import com.parimal.e_wholesaler.product_service.services.ProductService;
import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping
    public ProductResponseDTO addProduct(
            @RequestBody @Valid
            ProductRequestDTO requestDTO
    ) {
        return productService.addProduct(requestDTO);
    }

    @GetMapping(path = "/{id}")
    public ProductDTO getProductById(
            @PathVariable
            Long id
    ) {
        return productService.getProductById(id);
    }

    @DeleteMapping(path = "/{id}")
    public MessageDTO removeProductById(
            @PathVariable
            Long id
    ) {
        return productService.removeProductById(id);
    }




}
