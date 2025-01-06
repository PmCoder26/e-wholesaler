package com.parimal.e_wholesaler.customer_service.repositories;

import com.parimal.e_wholesaler.customer_service.entities.CustomerEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    boolean findByMobNo(Long mobNo);

}
