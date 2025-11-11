package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.order.Order;

public interface OrderRepositoryGateway {
	
	Order save(Order order);
	
}
