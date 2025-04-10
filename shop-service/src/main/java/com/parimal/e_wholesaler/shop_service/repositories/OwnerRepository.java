package com.parimal.e_wholesaler.shop_service.repositories;

import com.parimal.e_wholesaler.shop_service.entities.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {

    boolean existsByMobNo(Long mobNo);

    @Query(value = "SELECT o.id FROM OwnerEntity o WHERE o.mobNo = :mobNo")
    Optional<Long> findIdByMobNo(String mobNo);

}
