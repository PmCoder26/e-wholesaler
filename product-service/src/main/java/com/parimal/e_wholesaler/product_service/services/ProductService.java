package com.parimal.e_wholesaler.product_service.services;

import com.parimal.e_wholesaler.product_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.product_service.dtos.product.ProductDTO;
import com.parimal.e_wholesaler.product_service.dtos.product.ProductRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.product.ProductResponseDTO;
import com.parimal.e_wholesaler.product_service.entities.ProductEntity;
import com.parimal.e_wholesaler.product_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.product_service.exceptions.ResourceNotFoundException;
import com.parimal.e_wholesaler.product_service.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductResponseDTO addProduct(ProductRequestDTO requestDTO) {
        boolean productExists = productRepository.existsByName(requestDTO.getName());
        if(productExists) {
            throw new ResourceAlreadyExistsException("Product with name: " + requestDTO.getName() + " already exists.");
        }
        ProductEntity toSave = modelMapper.map(requestDTO, ProductEntity.class);
        ProductEntity saved = productRepository.save(toSave);
        return modelMapper.map(saved, ProductResponseDTO.class);
    }

    public ProductDTO getProductById(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found."));
        return modelMapper.map(product, ProductDTO.class);
    }

    public MessageDTO removeProductById(Long id) {
        boolean productExists = productRepository.existsById(id);
        if(!productExists) {
            throw new ResourceNotFoundException("Product with id: " + id + " not found.");
        }
        productRepository.deleteById(id);
        return new MessageDTO("Product deleted successfully");
    }

    Optional<ProductEntity> findById(Long id) {
        return productRepository.findById(id);
    }

    Optional<ProductEntity> findByName(String name) {
        return productRepository.findByName(name);
    }

    ProductEntity saveProduct(ProductEntity toSave) {
        return productRepository.save(toSave);
    }

}
