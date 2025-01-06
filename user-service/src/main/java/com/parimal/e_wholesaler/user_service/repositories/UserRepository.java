package com.parimal.e_wholesaler.user_service.repositories;

import com.parimal.e_wholesaler.user_service.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    Optional<UserDetails> findByUsername(String username);

}
