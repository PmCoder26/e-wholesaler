package com.parimal.e_wholesaler.order_service.utils;

public enum OrderStatus {
    CREATING,       // items are being added
    PENDING,        // all items are finally added but waiting for confirmation
    CONFIRMED,      // ordered is confirmed but not delivered
    DELIVERED       // order is delivered
}
