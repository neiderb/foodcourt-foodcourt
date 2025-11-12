package com.foodcourt.foodcourt.application.dto.request;

public record CreateOrderDishRequest(
	Long idDish,
	Integer quantity
) {}
