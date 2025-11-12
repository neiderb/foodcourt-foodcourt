package com.foodcourt.foodcourt.infrastructure.adapters.persistence.projection;

import java.time.LocalDateTime;

public interface OrderSummaryProjection {
	
	Long getIdOrder();
	LocalDateTime getOrderDate();
	String getStatus();
	
}
