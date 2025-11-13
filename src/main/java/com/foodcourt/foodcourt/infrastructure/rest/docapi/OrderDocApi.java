package com.foodcourt.foodcourt.infrastructure.rest.docapi;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderDocApi {
	
	public static final String TAG_ORDER = "Order Management";
	
	public static final String CREATE_ORDER_SUMMARY = "Create a new order";
	public static final String CREATE_ORDER_DESCRIPTION = "Order created successfully";
	
	public static final String LIST_ORDER_BY_RESTAURANT_SUMMARY = "List all orders by restaurant ID";
	public static final String LIST_ORDER_BY_RESTAURANT_DESCRIPTION = "List of orders by restaurant retrieved successfully";
	
	public static final String GET_ORDER_BY_ID_SUMMARY = "Get order by ID";
	public static final String GET_ORDER_BY_ID_DESCRIPTION = "Order retrieved successfully by ID";
	
}
