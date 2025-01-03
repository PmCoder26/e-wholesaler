package com.parimal.e_wholesaler.shop_service.entities;

import com.parimal.e_wholesaler.shop_service.utils.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "owners")
@AllArgsConstructor
@NoArgsConstructor
public class OwnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(unique = true, nullable = false, length = 10)
    private Long mobNo;

    private String address;

    private String city;

    private String state;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<ShopEntity> shops;

}
