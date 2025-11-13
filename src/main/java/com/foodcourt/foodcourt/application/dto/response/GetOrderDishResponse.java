package com.foodcourt.foodcourt.application.dto.response;

public record GetOrderDishResponse(
	Long idDish,
	String dishName,
	Long quantity
) {}

