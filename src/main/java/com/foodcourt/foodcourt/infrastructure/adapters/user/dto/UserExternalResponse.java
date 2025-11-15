package com.foodcourt.foodcourt.infrastructure.adapters.user.dto;

public record UserExternalResponse(
	Long id,
	String email,
	String role,
	String phone
) {}
