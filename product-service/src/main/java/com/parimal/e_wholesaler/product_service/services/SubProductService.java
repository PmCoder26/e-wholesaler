package com.parimal.e_wholesaler.product_service.services;

import com.parimal.e_wholesaler.product_service.dtos.sub_product.SubProductDTO;
import com.parimal.e_wholesaler.product_service.dtos.sub_product.SubProductRequestDTO;
import com.parimal.e_wholesaler.product_service.dtos.sub_product.SubProductResponseDTO;
import com.parimal.e_wholesaler.product_service.entities.ProductEntity;
import com.parimal.e_wholesaler.product_service.entities.SubProductEntity;
import com.parimal.e_wholesaler.product_service.repositories.SubProductRepository;
import com.parimal.e_wholesaler.shop_service.dtos.MessageDTO;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceAlreadyExistsException;
import com.parimal.e_wholesaler.shop_service.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class SubProductService {

    private final SubProductRepository subProductRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;


    public SubProductResponseDTO addSubProduct(SubProductRequestDTO requestDTO) {
        ProductEntity product = productService.findById(requestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + requestDTO.getProductId() + " not found."));
        Double MRP = requestDTO.getMRP();
        boolean subProductNotExists = product.getSubProducts()
                .stream()
                .filter(subProduct -> Objects.equals(subProduct.getMRP(), MRP))
                .toList()
                .isEmpty();
        if(!subProductNotExists) {
            throw new ResourceAlreadyExistsException("Sub-product with price: " + requestDTO.getMRP() + " already exists.");
        }
        SubProductEntity toSave = modelMapper.map(requestDTO, SubProductEntity.class);
        toSave.setProduct(product);
        SubProductEntity saved = subProductRepository.save(toSave);
        return modelMapper.map(saved, SubProductResponseDTO.class);
    }

    public SubProductDTO getSubProductById(Long id) {
        SubProductEntity subProduct = subProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sub-product with id: " + id + " not found."));
        return modelMapper.map(subProduct, SubProductDTO.class);
    }

    public MessageDTO removeSubProductById(Long id) {
        boolean subProductExists = subProductRepository.existsById(id);
        if(!subProductExists) {
            throw new ResourceNotFoundException("Sub-product with id: " + id + " not found.");
        }
        subProductRepository.deleteById(id);
        return new MessageDTO("Sub-product deleted successfully");
    }

    SubProductEntity addSubProduct(ProductEntity product, Double mrp) {
        SubProductEntity toSave = new SubProductEntity();
        toSave.setProduct(product);
        toSave.setMRP(mrp);
        return subProductRepository.save(toSave);
    }


}
