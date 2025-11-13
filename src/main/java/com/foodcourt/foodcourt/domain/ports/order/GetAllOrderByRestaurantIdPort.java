package com.foodcourt.foodcourt.domain.ports.order;

import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.order.OrderPaginationFilter;
import com.foodcourt.foodcourt.domain.model.order.OrderSummary;

public interface GetAllOrderByRestaurantIdPort {
	
	PaginationResponse<OrderSummary> execute(Long idRestaurant, OrderPaginationFilter filter);
	
}
