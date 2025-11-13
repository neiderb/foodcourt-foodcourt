package com.foodcourt.foodcourt.domain.model.order.enums;

import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderStatusException;

import static com.foodcourt.foodcourt.domain.constants.OrderErrorMessage.INVALID_ORDER_STATUS;

public enum OrderStatus {
	PENDING,
	PROCESSING,
	COMPLETED,
	DELIVERED,
	CANCELLED;
	
	public static OrderStatus of(String status) {
		for (OrderStatus orderStatus : OrderStatus.values()) {
			if (orderStatus.name().equalsIgnoreCase(status)) {
				return orderStatus;
			}
		}
		throw new InvalidOrderStatusException(INVALID_ORDER_STATUS);
	}
	
}
