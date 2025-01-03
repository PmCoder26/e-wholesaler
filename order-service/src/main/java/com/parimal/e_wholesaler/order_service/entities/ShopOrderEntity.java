package com.parimal.e_wholesaler.order_service.entities;

import com.parimal.e_wholesaler.order_service.utils.OrderStatus;
import com.parimal.e_wholesaler.order_service.utils.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "shop_orders")
@AllArgsConstructor
@NoArgsConstructor
public class ShopOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Enumerated(value = EnumType.STRING)
    private PaymentMethod payment;

    private Long workerId;

    @CreatedDate
    private LocalDateTime createdAt;

}
