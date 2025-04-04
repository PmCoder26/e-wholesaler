package com.parimal.e_wholesaler.shop_service.repositories;

import com.parimal.e_wholesaler.shop_service.entities.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {

    boolean existsByMobNo(Long mobNo);

}
