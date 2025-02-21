package com.parimal.e_wholesaler.shop_service.repositories;

import com.parimal.e_wholesaler.shop_service.entities.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Long> {

    boolean existsByNameAndGstNo(String name,String gstNo);

    List<ShopEntity> findByOwner_Id(Long ownerId);


}
