package com.parimal.e_wholesaler.order_service.repositories;

import com.parimal.e_wholesaler.order_service.entities.ShopOrderEntity;
import com.parimal.e_wholesaler.order_service.utils.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrderEntity, Long> {


    Long countByShopIdAndStatus(Long shopId, OrderStatus status);

//    @Query(value = "SELECT COUNT(o) FROM ShopOrderEntity o WHERE o.shopId = :shopId AND o.createdAt >= :startOfDay AND o.status = :status")
//    Long countByShopIdAndForToday(Long shopId, LocalDateTime startOfDay, OrderStatus status);

    Long countByShopIdAndStatusAndCreatedAtBetween(Long shopId, OrderStatus status, LocalDateTime startOfDay, LocalDateTime endOfDay);

}
