package com.parimal.e_wholesaler.order_service.dtos.route;

import com.parimal.e_wholesaler.order_service.utils.OrderStatus;
import com.parimal.e_wholesaler.order_service.utils.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteOrderDTO {

    private Long id;

    private Double amount;

    private OrderStatus status;

    private PaymentMethod payment;

    private Long workerId;

    private Long customerId;

    private LocalDateTime createdAt;

}
