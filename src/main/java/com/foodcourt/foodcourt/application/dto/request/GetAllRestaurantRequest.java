package com.foodcourt.foodcourt.application.dto.request;

public record GetAllRestaurantRequest(
	int page,
	int size,
	String sortBy,
	String sortDirection
) {}
