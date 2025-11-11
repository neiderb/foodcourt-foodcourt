package com.foodcourt.foodcourt.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderValidationMessage {
	
	public static final String INVALID_CLIENT = "The client associated with the order is invalid";
	public static final String INVALID_ORDER = "The order is invalid";
	
	public static final String CHEF_IS_REQUIRED = "Chef is required to process the order";
	public static final String DISH_NOT_AVAILABLE = "One or more dishes in the order are not available";
	public static final String RESTAURANT_IS_REQUIRED = "Restaurant is required to process the order";
	public static final String ORDER_MUST_HAVE_AT_LEAST_ONE_ITEM = "The order must contain at least one item";
	
}
