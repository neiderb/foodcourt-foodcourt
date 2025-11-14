package com.foodcourt.foodcourt.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.foodcourt.foodcourt.domain.constants.OrderValidationMessage.*;

public record CreateOrderDishRequest(
	@NotNull(message = DISH_ID_IS_REQUIRED)
	@Positive(message = DISH_ID_MUST_BE_POSITIVE)
	Long idDish,
	
	@NotNull(message = QUANTITY_IS_REQUIRED)
	@Positive(message = QUANTITY_MUST_BE_POSITIVE)
	Integer quantity
) {}
