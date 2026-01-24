package com.parimal.e_wholesaler.product_service.repositories;

import com.parimal.e_wholesaler.product_service.entities.SubProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubProductRepository extends JpaRepository<SubProductEntity, Long> {

    Optional<SubProductEntity> findByProduct_IdAndMrp(Long id, Double mrp);

}
