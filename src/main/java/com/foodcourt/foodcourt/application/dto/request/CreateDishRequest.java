package com.foodcourt.foodcourt.application.dto.request;

public record CreateDishRequest(
	String name,
	Long idCategory,
	String description,
	Double price,
	Long idRestaurant,
	String imageUrl,
	Boolean isAvailable
) {}
