package com.foodcourt.foodcourt.application.dto.request;

public record GetAllDishByRestaurantIdRequest(
	Integer page,
	Integer size,
	String sortBy,
	String sortDirection,
	Long idCategory
) {}
