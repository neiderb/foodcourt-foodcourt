package com.foodcourt.foodcourt.domain.model.auth;

import com.foodcourt.foodcourt.domain.model.auth.enums.UserRole;

public record UserClaims(
	Long id,
	String email,
	UserRole role,
	Long idRestaurant
) {}
