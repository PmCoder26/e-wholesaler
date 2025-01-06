package com.parimal.e_wholesaler.product_service.repositories;

import com.parimal.e_wholesaler.product_service.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}
