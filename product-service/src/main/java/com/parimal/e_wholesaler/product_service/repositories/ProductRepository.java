package com.parimal.e_wholesaler.product_service.repositories;

import com.parimal.e_wholesaler.common.dtos.product.ProductIdentityDTO;
import com.parimal.e_wholesaler.product_service.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByNameIgnoreCaseAndCompanyIgnoreCase(String productName, String company);

    @Query(
            value = """
                    SELECT DISTINCT new com.parimal.e_wholesaler.common.dtos.product.ProductIdentityDTO(
                        p.id, p.name, p.company
                    )
                    FROM ProductEntity p
                    JOIN p.subProducts sp
                    JOIN sp.shopSubProducts ssp
                    WHERE ssp.shopId = :shopId
                    """)
    List<ProductIdentityDTO> findShopProductsByShopId(Long shopId);

}
