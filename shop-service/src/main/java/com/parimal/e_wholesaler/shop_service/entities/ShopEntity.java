package com.parimal.e_wholesaler.shop_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "shops")
@AllArgsConstructor
@NoArgsConstructor
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String gstNo;

    private String address;

    private String city;

    private String state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private OwnerEntity owner;

    @CreatedDate
    private LocalDateTime createdAt;

}
