package com.parimal.e_wholesaler.product_service.repositories;

import com.parimal.e_wholesaler.product_service.entities.ShopSubProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopSubProductRepository extends JpaRepository<ShopSubProductEntity, Long> {

    Optional<ShopSubProductEntity> findByIdAndShopId(Long subProductId, Long shopId);

    List<ShopSubProductEntity> findByShopId(Long shopId);

    long deleteAllByShopIdAndSubProduct_Product_Name(Long shopId, String productName);
}
