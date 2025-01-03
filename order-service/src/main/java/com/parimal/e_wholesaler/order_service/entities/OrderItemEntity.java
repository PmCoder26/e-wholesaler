package com.parimal.e_wholesaler.order_service.entities;

import com.parimal.e_wholesaler.order_service.utils.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long subProductId;

    private Long quantity;

    private Double price;

    @Enumerated(value = EnumType.STRING)
    private OrderType orderType;

    private Long orderId;

}
