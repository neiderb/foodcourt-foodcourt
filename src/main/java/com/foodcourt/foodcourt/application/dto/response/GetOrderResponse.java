package com.foodcourt.foodcourt.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record GetOrderResponse(
	Long idOrder,
	Long idClient,
	LocalDateTime orderDate,
	String status,
	Long idChef,
	Long idRestaurant,
	List<GetOrderDishResponse> dishes
) {}
