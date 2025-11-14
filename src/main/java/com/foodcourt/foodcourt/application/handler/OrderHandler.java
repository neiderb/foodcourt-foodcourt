package com.foodcourt.foodcourt.application.handler;

import com.foodcourt.foodcourt.application.dto.request.CreateOrderRequest;
import com.foodcourt.foodcourt.application.dto.request.GetAllOrderByRestaurantIdRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateOrderResponse;
import com.foodcourt.foodcourt.application.dto.response.GetOrderResponse;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.order.OrderSummary;

public interface OrderHandler {
	
	CreateOrderResponse createOrder(CreateOrderRequest request);
	
	PaginationResponse<OrderSummary> getAllOrdersByRestaurantId(GetAllOrderByRestaurantIdRequest request);
	
	GetOrderResponse getOrderById(Long idOrder);
	
	void assignOrder(Long idOrder);
	
	void completeOrder(Long idOrder);
	
	void deliverOrder(Long idOrder, String code);
	
}
