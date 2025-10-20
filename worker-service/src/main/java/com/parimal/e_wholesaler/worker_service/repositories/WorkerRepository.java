package com.parimal.e_wholesaler.worker_service.repositories;

import com.parimal.e_wholesaler.worker_service.entities.WorkerEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<WorkerEntity, Long> {

    boolean existsByMobNo(String mobNo);

    boolean existsByIdAndShopId(Long workerId, Long shopId);

    long countByShopId(Long shopId);

    List<WorkerEntity> findByShopId(Long shopId);

    Optional<WorkerEntity> findByMobNo(String mobNo);

    @Query(value = "SELECT w.id FROM WorkerEntity w WHERE w.mobNo = :mobNo")
    Optional<Long> findIdByMobNo(String mobNo);

    List<WorkerEntity> findAllByShopId(Long shopId);

}
