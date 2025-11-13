package com.foodcourt.foodcourt.domain.ports.order;

import com.foodcourt.foodcourt.domain.model.order.Order;

public interface CreateOrderPort {
	
	Order execute(Order order);
	
}
