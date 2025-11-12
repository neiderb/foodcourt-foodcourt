package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.OrderPaginationFilter;
import com.foodcourt.foodcourt.domain.model.order.OrderStatus;
import com.foodcourt.foodcourt.domain.model.order.OrderSummary;

import java.util.Set;

public interface OrderRepositoryGateway {
	
	Order save(Order order);
	
	boolean existActiveOrderByClientId(Long idClient, Set<OrderStatus> activeStatus);
	
	PaginationResponse<OrderSummary> findAllByRestaurantId(Long idRestaurant, OrderPaginationFilter filter);
	
}
