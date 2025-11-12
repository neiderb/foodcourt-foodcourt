package com.foodcourt.foodcourt.application.dto.request;

import java.util.List;

public record CreateOrderRequest(
	Long idClient,
	Long idRestaurant,
	List<CreateOrderDishRequest> items
) {}
