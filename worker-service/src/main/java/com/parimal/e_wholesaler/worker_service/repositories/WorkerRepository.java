package com.parimal.e_wholesaler.worker_service.repositories;

import com.parimal.e_wholesaler.worker_service.entities.WorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository<WorkerEntity, Long> {

    boolean existsByMobNo(String mobNo);

    boolean existsByIdAndShopId(Long workerId, Long shopId);

}
