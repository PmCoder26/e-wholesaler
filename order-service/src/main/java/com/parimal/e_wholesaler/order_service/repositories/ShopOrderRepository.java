package com.parimal.e_wholesaler.order_service.repositories;

import com.parimal.e_wholesaler.order_service.entities.ShopOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrderEntity, Long> {

    long countByShopId(Long shopId);

}
