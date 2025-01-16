package com.parimal.e_wholesaler.sales_service.repositories;

import com.parimal.e_wholesaler.sales_service.entities.DailySalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SalesRepository extends JpaRepository<DailySalesEntity, Long> {

    boolean existsByCreatedAtAndShopId(LocalDate now, Long shopId);

    Optional<DailySalesEntity> findByCreatedAtAndShopId(LocalDate createdAt, Long shopId);

}
