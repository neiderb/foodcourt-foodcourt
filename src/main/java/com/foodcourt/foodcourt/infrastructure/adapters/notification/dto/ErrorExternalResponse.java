package com.foodcourt.foodcourt.infrastructure.adapters.notification.dto;

public record ErrorExternalResponse(
	int code,
	String message
) {}
