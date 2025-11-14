package com.foodcourt.foodcourt.infrastructure.rest.constants.paths;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderPath {
	
	public static final String BASE = "/api/v1/order";
	
	public static final String ORDER_BY_ID = "/{idOrder}";
	public static final String ASSIGN_ORDER_BY_ID = "/assign/{idOrder}";
	public static final String COMPLETE_ORDER_BY_ID = "/complete/{idOrder}";
	public static final String DELIVER_ORDER_BY_ID = "/deliver/{idOrder}/code/{code}";
	public static final String CANCEL_ORDER_BY_ID = "/cancel/{idOrder}";
	
}
