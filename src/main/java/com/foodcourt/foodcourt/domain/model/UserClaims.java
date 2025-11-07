package com.foodcourt.foodcourt.domain.model;

public record UserClaims(
	Long id,
	String email,
	UserRole role
) {}
