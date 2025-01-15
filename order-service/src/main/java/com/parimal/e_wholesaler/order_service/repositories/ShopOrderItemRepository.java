package com.parimal.e_wholesaler.order_service.repositories;

import com.parimal.e_wholesaler.order_service.entities.ShopOrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopOrderItemRepository extends JpaRepository<ShopOrderItemEntity, Long> {

}
