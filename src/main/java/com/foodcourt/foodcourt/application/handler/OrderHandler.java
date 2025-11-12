package com.foodcourt.foodcourt.application.handler;

import com.foodcourt.foodcourt.application.dto.request.CreateOrderRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateOrderResponse;

public interface OrderHandler {
	
	CreateOrderResponse createOrder(CreateOrderRequest request);
	
}
