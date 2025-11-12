package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.OrderStatus;

import java.util.Set;

public interface OrderRepositoryGateway {
	
	Order save(Order order);
	
	boolean existActiveOrderByClientId(Long idClient, Set<OrderStatus> activeStatus);
	
}
