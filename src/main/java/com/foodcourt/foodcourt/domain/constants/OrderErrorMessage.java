package com.foodcourt.foodcourt.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderErrorMessage {
	
	public static final String INVALID_ORDER_STATUS = "Invalid order status";
	public static final String DISH_NOT_AVAILABLE = "One or more dishes in the order are not available";
	public static final String CLIENT_HAS_ACTIVE_ORDER = "The client already has an active order";
	public static final String ORDER_NOT_FOUND = "Order not found";
	public static final String ORDER_STATUS_MUST_BE_PENDING = "Order status must be PENDING to perform this action";
	public static final String ORDER_STATUS_MUST_BE_PROCESSING = "Order status must be PROCESSING to perform this action";
	public static final String ORDER_STATUS_MUST_BE_COMPLETED = "Order status must be COMPLETED to perform this action";
	public static final String ORDER_MUST_BE_PENDING_TO_CANCEL = "We're sorry, your order is already being prepared and cannot be canceled";
	public static final String INVALID_SECURE_PIN = "Invalid secure PIN provided";
	
}
