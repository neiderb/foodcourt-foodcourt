package com.foodcourt.foodcourt.domain.ports;

import com.foodcourt.foodcourt.domain.model.order.Order;

public interface CreateOrderPort {
	
	Order execute(Order order);
	
}
