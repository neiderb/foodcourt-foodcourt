package com.foodcourt.foodcourt.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderErrorMessage {
	
	public static final String INVALID_ORDER_STATUS = "Invalid order status";
	public static final String DISH_NOT_AVAILABLE = "One or more dishes in the order are not available";
	public static final String CLIENT_HAS_ACTIVE_ORDER = "The client already has an active order";
	public static final String ORDER_NOT_FOUND = "Order not found";
	
}
