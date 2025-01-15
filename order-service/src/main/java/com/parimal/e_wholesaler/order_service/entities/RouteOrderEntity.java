package com.parimal.e_wholesaler.order_service.entities;

import com.parimal.e_wholesaler.order_service.utils.OrderStatus;
import com.parimal.e_wholesaler.order_service.utils.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "route_orders")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RouteOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount = 0.0;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Enumerated(value = EnumType.STRING)
    private PaymentMethod payment;

    private Long workerId;

    private Long customerId;

    @OneToMany(mappedBy = "routeOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RouteOrderItemEntity> routeOrderItems;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

}
