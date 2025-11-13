package com.foodcourt.foodcourt.application.dto.request;

public record GetAllOrderByRestaurantIdRequest(
	int page,
	int size,
	String sortBy,
	String sortDirection,
	String status
) {}
