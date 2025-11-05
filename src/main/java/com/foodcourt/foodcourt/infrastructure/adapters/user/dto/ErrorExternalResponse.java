package com.foodcourt.foodcourt.infrastructure.adapters.user.dto;

public record ErrorExternalResponse(
	int code,
	String message
) {}
