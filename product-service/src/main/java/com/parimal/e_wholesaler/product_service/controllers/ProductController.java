package com.parimal.e_wholesaler.product_service.controllers;

import com.parimal.e_wholesaler.product_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.product_service.dtos.product.ProductDTO;
import com.parimal.e_wholesaler.product_service.dtos.product.ProductRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.product.ProductResponseDTO;
import com.parimal.e_wholesaler.product_service.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
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
            HttpServletRequest request,
            @RequestBody @Valid ProductRequestDTO requestDTO
    ) {
        return productService.addProduct(request, requestDTO);
    }

    @GetMapping(path = "/{id}")
    public ProductDTO getProductById(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        return productService.getProductById(request, id);
    }

    @DeleteMapping(path = "/{id}")
    public MessageDTO removeProductById(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        return productService.removeProductById(request, id);
    }




}
