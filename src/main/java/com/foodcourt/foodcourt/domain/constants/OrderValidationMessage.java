package com.foodcourt.foodcourt.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderValidationMessage {
	
	public static final String INVALID_CLIENT = "The client associated with the order is invalid";
	public static final String INVALID_ORDER = "The order is invalid";
	public static final String INVALID_ORDER_ID = "The order ID is invalid";
	public static final String INVALID_RESTAURANT_ID = "The restaurant ID is invalid";
	
	public static final String RESTAURANT_IS_REQUIRED = "Restaurant is required to process the order";
	public static final String ORDER_MUST_HAVE_AT_LEAST_ONE_ITEM = "The order must contain at least one item";
	public static final String ITEM_QUANTITY_MUST_BE_GREATER_THAN_ZERO = "The quantity of each item in the order must be greater than zero";
	public static final String ORDER_CANNOT_HAVE_DUPLICATE_DISHES = "The order cannot contain duplicate dishes";
	
	public static final String DISH_ID_IS_REQUIRED = "Dish ID is required for each item in the order";
	public static final String DISH_ID_MUST_BE_POSITIVE = "Dish ID must be a positive number";
	public static final String QUANTITY_IS_REQUIRED = "Quantity is required for each item in the order";
	public static final String QUANTITY_MUST_BE_POSITIVE = "Quantity must be a positive number";
	
}
