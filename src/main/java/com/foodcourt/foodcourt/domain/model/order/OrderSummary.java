package com.foodcourt.foodcourt.domain.model.order;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderSummary {
	
	private Long idOrder;
	private LocalDateTime orderDate;
	private String status;
	
}
