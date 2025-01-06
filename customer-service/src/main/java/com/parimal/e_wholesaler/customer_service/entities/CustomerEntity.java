package com.parimal.e_wholesaler.customer_service.entities;

import com.parimal.e_wholesaler.customer_service.utils.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, length = 10, unique = true)
    private Long mobNo;

    private String address;

    private String city;

    private String state;

    @CreatedDate
    LocalDateTime createdAt;

}
