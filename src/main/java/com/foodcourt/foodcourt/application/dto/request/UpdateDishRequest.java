package com.foodcourt.foodcourt.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.foodcourt.foodcourt.domain.constants.DishValidationMessage.*;

public record UpdateDishRequest(
	@NotNull(message = ID_REQUIRED)
	@Positive(message = ID_MUST_BE_MORE_THAN_ZERO)
	Long id,
	
	@NotNull(message = PRICE_REQUIRED)
	@Positive(message = PRICE_MUST_BE_MORE_THAN_ZERO)
	Long price,
	
	@NotNull(message = DESCRIPTION_REQUIRED)
	@NotBlank(message = DESCRIPTION_REQUIRED)
	String description
) {}
