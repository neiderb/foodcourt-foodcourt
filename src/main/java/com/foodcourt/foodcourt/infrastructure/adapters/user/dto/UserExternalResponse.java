package com.foodcourt.foodcourt.infrastructure.adapters.user.dto;

public record UserExternalResponse(
	Long id,
	String name,
	String role,
	String phone
) {}
