package com.foodcourt.foodcourt.infrastructure.adapters.traceability.dto;

public record ExternalOrderTraceInput(
	Long idOrder,
	Long idClient,
	String emailClient,
	String previousStatus,
	String newStatus,
	Long idEmployee,
	String emailEmployee
) {}
