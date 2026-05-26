package com.ice.common.constant;

import java.util.Set;

public final class OrderStatus {
    public static final String PENDING = "pending";
    public static final String PAID = "paid";
    public static final String PREPARING = "preparing";
    public static final String DELIVERING = "delivering";
    public static final String DELIVERED = "delivered";
    public static final String CANCELLED = "cancelled";

    private static final Set<String> VALUES = Set.of(PENDING, PAID, PREPARING, DELIVERING, DELIVERED, CANCELLED);

    private OrderStatus() {
    }

    public static boolean isValid(String status) {
        return VALUES.contains(status);
    }
}
