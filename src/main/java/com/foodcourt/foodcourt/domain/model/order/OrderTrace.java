package com.foodcourt.foodcourt.domain.model.order;

import com.foodcourt.foodcourt.domain.model.order.enums.OrderStatus;

public record OrderTrace(
	Long idOrder,
	Long idClient,
	String emailClient,
	OrderStatus previousStatus,
	OrderStatus newStatus,
	Long idEmployee,
	String emailEmployee
) {}
