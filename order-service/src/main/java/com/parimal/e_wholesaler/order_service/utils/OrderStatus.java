package com.parimal.e_wholesaler.order_service.utils;

public enum OrderStatus {
    CREATING,       // items are being added
    CONFIRMED,      // ordered is confirmed but not delivered
    DELIVERED       // order is delivered
}
