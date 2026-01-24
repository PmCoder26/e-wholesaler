package com.parimal.e_wholesaler.product_service.entities;

import com.parimal.e_wholesaler.common.enums.UnitType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Table(
        name = "shop_selling_units",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "shop_sub_product_id", "unit_type" })
        }
)
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(value = AuditingEntityListener.class)
public class ShopSellingUnitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_sub_product_id", nullable = false)
    private ShopSubProductEntity shopSubProduct;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "unit_type", nullable = false)
    private UnitType unitType;      // piece, box, sack, etc,

    // no. of pieces in the unitType.
    @Column(nullable = false)
    private Integer packets;

    // selling price of unitType.
    @Column(nullable = false)
    private Double sellingPrice;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

}
