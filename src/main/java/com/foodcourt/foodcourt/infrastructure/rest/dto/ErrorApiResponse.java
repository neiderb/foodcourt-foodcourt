package com.foodcourt.foodcourt.infrastructure.rest.dto;

public record ErrorApiResponse(
	int code,
	String message
) {}
