package com.parimal.e_wholesaler.product_service.repositories;

import com.parimal.e_wholesaler.common.enums.UnitType;
import com.parimal.e_wholesaler.product_service.entities.ShopSellingUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellingUnitRepository extends JpaRepository<ShopSellingUnitEntity, Long> {

    Optional<ShopSellingUnitEntity> findByShopSubProduct_IdAndUnitType(Long id, UnitType unitType);

    boolean existsByShopSubProduct_IdAndUnitType(Long id, UnitType unitType);

    Optional<ShopSellingUnitEntity> findByIdAndShopSubProduct_IdAndShopSubProduct_ShopId(Long sellingUnitId, Long shopSubProductId, Long shopId);

}
