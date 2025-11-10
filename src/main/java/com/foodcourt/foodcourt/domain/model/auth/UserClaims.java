package com.foodcourt.foodcourt.domain.model.auth;

public record UserClaims(
	Long id,
	String email,
	UserRole role
) {}
