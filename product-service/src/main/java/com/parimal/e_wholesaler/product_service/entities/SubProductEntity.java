package com.parimal.e_wholesaler.product_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sub_products")
@AllArgsConstructor
@NoArgsConstructor
public class SubProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;

    private Long stock;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @CreatedDate
    private LocalDateTime createdAt;
}
