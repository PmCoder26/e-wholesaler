package com.parimal.e_wholesaler.order_service.repositories;

import com.parimal.e_wholesaler.order_service.entities.RouteOrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteOrderItemRepository extends JpaRepository<RouteOrderItemEntity, Long> {

}
