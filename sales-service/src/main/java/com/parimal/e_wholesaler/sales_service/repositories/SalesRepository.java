package com.parimal.e_wholesaler.sales_service.repositories;

import com.parimal.e_wholesaler.sales_service.entities.DailySalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SalesRepository extends JpaRepository<DailySalesEntity, Long> {

    boolean existsByCreatedAtAndShopId(LocalDate now, Long shopId);

    Optional<DailySalesEntity> findByCreatedAtAndShopId(LocalDate createdAt, Long shopId);

    @Query("SELECT d.amount FROM DailySalesEntity d WHERE d.shopId = :shopId AND d.createdAt = :createdAt")
    Optional<Double> findAmountByShopIdAndCreatedAt(Long shopId, LocalDate createdAt);

    Optional<DailySalesEntity> findByShopIdAndCreatedAt(Long shopId, LocalDate createdAt);

}