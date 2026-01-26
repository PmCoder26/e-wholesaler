package com.parimal.e_wholesaler.product_service.repositories;

import com.parimal.e_wholesaler.product_service.entities.ShopSubProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopSubProductRepository extends JpaRepository<ShopSubProductEntity, Long> {

    Optional<ShopSubProductEntity> findByIdAndShopId(Long subProductId, Long shopId);

    Optional<ShopSubProductEntity> findByShopIdAndSubProduct_Id(Long shopId, Long id);

    @Query(
           value = """
                   SELECT ssp FROM ShopSubProductEntity ssp
                   JOIN FETCH ssp.sellingUnits
                   WHERE ssp.shopId = :shopId
                   AND ssp.subProduct.product.id = :productId
                   """
    )
    List<ShopSubProductEntity> findByShopIdAndProductId(Long shopId, Long productId);

    long deleteAllByShopIdAndSubProduct_Product_Id(Long shopId, Long productId);

}
