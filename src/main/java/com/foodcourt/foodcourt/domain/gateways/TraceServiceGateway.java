package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.order.OrderTrace;

public interface TraceServiceGateway {
	
	void saveOrderTrace(OrderTrace orderTrace);
	
}
