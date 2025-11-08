package com.foodcourt.foodcourt.application.dto.response;

public record RestaurantResponse(
	Long id,
	String name,
	String nit,
	String address,
	String phoneNumber,
	String urlLogo,
	Long ownerId
) {}
