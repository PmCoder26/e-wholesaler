package com.parimal.e_wholesaler.sales_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDate;


@Data
@Entity
@Table(name = "daily_sales")
@AllArgsConstructor
@NoArgsConstructor
public class DailySalesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private Long shopId;

    @CreatedDate
    private LocalDate createdAt;

}
